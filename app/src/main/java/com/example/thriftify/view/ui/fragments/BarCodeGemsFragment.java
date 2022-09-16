package com.example.thriftify.view.ui.fragments;

import android.Manifest;
import android.app.ProgressDialog;
import android.content.DialogInterface;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.net.Uri;
import android.os.Bundle;
import android.provider.Settings;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.Toast;

import com.budiyev.android.codescanner.CodeScanner;
import com.budiyev.android.codescanner.CodeScannerView;
import com.budiyev.android.codescanner.DecodeCallback;
import com.example.thriftify.R;
import com.example.thriftify.databinding.FragmentBarCodedGemsBinding;
import com.example.thriftify.service.model.scan_model.ProductDetail;
import com.example.thriftify.service.respository.ProductDetailRepository;
import com.example.thriftify.utils.InternetUtils;
import com.example.thriftify.utils.SharedPreferencesUtils;
import com.example.thriftify.view.ui.ScanningDetailsGemActivity;
import com.example.thriftify.viewmodel.ProductDetailViewModel;
import com.example.thriftify.viewmodel.SingletonViewModelFactory;
import com.google.android.material.dialog.MaterialAlertDialogBuilder;
import com.google.zxing.Result;

import javax.inject.Inject;

import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.core.content.ContextCompat;
import androidx.databinding.DataBindingUtil;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;

import static com.example.thriftify.utils.Constants.CAMERA;


@AndroidEntryPoint
public class BarCodeGemsFragment extends Fragment {
    //Constants
    private static final String TAG = "BarCodeGemsFragment";
    ProgressDialog progressDialog;

    //Variables
    private CodeScanner mCodeScanner;
    private LifecycleOwner viewLifecycleOwner;
    private static boolean networkConnectionStatus = false;
    private ProductDetailViewModel model;
    private FragmentBarCodedGemsBinding binding;

    @Inject
    ProductDetailRepository repository;
    
    ActivityResultLauncher<String> requestPermissionLauncher = registerForActivityResult(new ActivityResultContracts.RequestPermission(), isGranted ->{
        if (isGranted) {
            // Permission is granted. Continue the action or workflow in your
            // app.
            Log.e(BarCodeGemsFragment.TAG, "Permission: Granted Done");
            // Permission is already available, start camera preview
            this.mCodeScanner.startPreview();
        } else {
            // Explain to the user that the feature is unavailable because the
            // features requires a permission that the user has denied. At the
            // same time, respect the user's decision. Don't link to system
            // settings in an effort to convince the user to change their
            // decision.
             if (shouldShowRequestPermissionRationale(Manifest.permission.CAMERA)) {
                // In an educational UI, explain to the user why your app requires this
                // permission for a specific feature to behave as expected. In this UI,
                // include a "cancel" or "no thanks" button that allows the user to
                // continue using your app without granting the permission.
                //  showInContextUI(...);
                 SharedPreferencesUtils.setDefaults(CAMERA, false, getActivity());
                ScanFragment fragment=((ScanFragment)getParentFragment());
                fragment.mViewPager.setCurrentItem(1);


            } else {
                 SharedPreferencesUtils.setDefaults(CAMERA, true, getActivity());
                 ScanFragment fragment=((ScanFragment)getParentFragment());
                 fragment.mViewPager.setCurrentItem(1);

            }

        }

    });


    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }


    @Nullable
    @Override
    public View onCreateView(@NonNull final LayoutInflater inflater, @Nullable final ViewGroup container, @Nullable final Bundle savedInstanceState) {
        Log.i(TAG, "onCreateView: CREATED");
        
        model = new ViewModelProvider(this, SingletonViewModelFactory.getInstance(repository)).get(ProductDetailViewModel.class);
        // Obtain binding
        binding = DataBindingUtil.inflate(inflater, R.layout.fragment_bar_coded_gems, container, false);
        final View view = binding.getRoot();
        // Bind layout with ViewModel
        binding.setScan(model);
        // LiveData needs the lifecycle owner
        this.viewLifecycleOwner = this.getViewLifecycleOwner();
        binding.setLifecycleOwner(this.viewLifecycleOwner);
        this.progressDialog = new ProgressDialog(getContext());
        this.progressDialog.setMessage("Loading..."); // Setting Message
        this.progressDialog.setTitle("Please Wait"); // Setting Title
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setCancelable(false);
        //Widgets
        CodeScannerView mCodeScannerView = view.findViewById(R.id.scanner_view);
        this.mCodeScanner = new CodeScanner(this.getActivity(), mCodeScannerView);
        this.mCodeScanner.setTouchFocusEnabled(true);
        //this.mCodeScanner.setFormats(Collections.singletonList(BarcodeFormat.EAN_13));
        this.mCodeScanner.setFormats(CodeScanner.ONE_DIMENSIONAL_FORMATS);
        InternetUtils.get(getContext()).observe(getActivity(), new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    networkConnectionStatus = aBoolean;
                }
                else {
                    networkConnectionStatus = aBoolean;
                    Toast.makeText(BarCodeGemsFragment.this.getActivity(), "No Internet Connection.", Toast.LENGTH_LONG).show();
                }
            }
        });
        this.mCodeScanner.setDecodeCallback(new DecodeCallback() {
            @Override
            public void onDecoded(@NonNull final Result result) {
                BarCodeGemsFragment.this.getActivity().runOnUiThread(new Runnable() {
                    @Override
                    public void run() {
                        Toast.makeText(BarCodeGemsFragment.this.getActivity(), result.getText(), Toast.LENGTH_SHORT).show();

                        if(networkConnectionStatus){
                            progressDialog.show();
                            model.getProductValidationDetails(Long.parseLong(result.getText()), BarCodeGemsFragment.this.getContext());
                            model.productDetailMutableLiveData.observe(BarCodeGemsFragment.this.getActivity(), new Observer<ProductDetail>() {
                                @Override
                                public void onChanged(final ProductDetail productDetail) {
                                    progressDialog.dismiss();

                                   if(model.productDetailMutableLiveData.getValue()==null){
                                       //do nothing
                                       Log.e(TAG, "onChanged: null");
                                       mCodeScanner.startPreview();
                                   }else{
                                       if(model.productDetailMutableLiveData.getValue().getError()!=null) {
                                           Log.e(TAG, "onChanged: with error");
                                           binding.invalidateAll();
                                       }else{
                                           Log.e(TAG, "onChanged: without error");
                                           binding.invalidateAll();
                                           Intent mIntent = new Intent(getActivity(), ScanningDetailsGemActivity.class);
                                           startActivity(mIntent);
                                       }
                                   }
                                }

                            });
                        }else {
                            Toast.makeText(BarCodeGemsFragment.this.getActivity(),"Mobile data is disabled. Connect to Wi-Fi network instead, or enable mobile data and try again", Toast.LENGTH_LONG).show();
                        }

                    }
                });
            }
        });

        mCodeScannerView.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {
                model.setNullifyProperty();
                binding.invalidateAll();

            }
        });


        return view;

    }

    @Override
    public void onViewCreated(@NonNull View view, @Nullable Bundle savedInstanceState) {
        super.onViewCreated(view, savedInstanceState);
        Log.i(TAG, "onViewCreated: CREATED");
    }

    @Override
    public void onStart() {
        super.onStart();
        Log.i(TAG, "onStart: STARTED");
    }

    @Override
    public void onResume() {
        super.onResume();
        Log.i(TAG, "onResume: RESUMED");

        if(ContextCompat.checkSelfPermission(this.getActivity(), Manifest.permission.CAMERA) == PackageManager.PERMISSION_GRANTED){
            this.mCodeScanner.startPreview();
        }else{
            if(!SharedPreferencesUtils.getDefaults(CAMERA, getActivity())){
                showConfirmationDialog(R.string.continues);
            }else {
                showConfirmationDialog(R.string.ok);
            }

        }
    }

    @Override
    public void onPause() {
        super.onPause();
        Log.i(TAG, "onPause: Pause-STARTED");
        this.mCodeScanner.releaseResources();
    }
    @Override
    public void onStop() {
        super.onStop();
        Log.i(TAG, "onStop: Stop-CREATED");
        this.mCodeScanner.releaseResources();

    }

    @Override
    public void onDestroy() {
        super.onDestroy();
        Log.i(TAG, "onDestroy: DESTROYED");
    }

    void showConfirmationDialog(int id){

        if(id == R.string.ok){
            new MaterialAlertDialogBuilder(getActivity())
                    .setTitle("Informative")
                    .setMessage("If you want to use this feature you have to setting to allow it.")
                    .setNeutralButton("Not", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ScanFragment fragment=((ScanFragment)getParentFragment());
                            fragment.mViewPager.setCurrentItem(1);
                        }
                    })
                    .setPositiveButton(id, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                            Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                            intent.setData(uri);
                            startActivity(intent);

                        }
                    }).show();

        }else{
            new MaterialAlertDialogBuilder(getActivity())
                    .setTitle("Informative")
                    .setMessage("You need this permission for using this features")
                    .setNeutralButton("Not", new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            ScanFragment fragment=((ScanFragment)getParentFragment());
                            fragment.mViewPager.setCurrentItem(1);
                        }
                    })
                    .setPositiveButton(id, new DialogInterface.OnClickListener() {
                        @Override
                        public void onClick(DialogInterface dialog, int which) {
                            dialog.dismiss();
                            // You can directly ask for the permission.
                            // The registered ActivityResultCallback gets the result of this request.
                            requestPermissionLauncher.launch(Manifest.permission.CAMERA);

                        }
                    }).show();
        }




    }


}
