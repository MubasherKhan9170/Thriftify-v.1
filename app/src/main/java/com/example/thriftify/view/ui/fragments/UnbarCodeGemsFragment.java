package com.example.thriftify.view.ui.fragments;

import android.app.Activity;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.graphics.Bitmap;
import android.net.Uri;
import android.os.Build;
import android.os.Bundle;
import android.os.Environment;
import android.provider.MediaStore;
import android.provider.Settings;
import android.util.Log;
import android.view.ActionMode;
import android.view.LayoutInflater;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.MenuItem;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AbsListView;
import android.widget.ArrayAdapter;
import android.widget.AutoCompleteTextView;
import android.widget.Button;
import android.widget.GridView;

import com.example.thriftify.R;
import com.example.thriftify.service.respository.ProductDetailRepository;
import com.example.thriftify.utils.ExpandableGridView;
import com.example.thriftify.utils.PermissionUtility;
import com.example.thriftify.utils.SharedPreferencesUtils;
import com.example.thriftify.view.adapters.GridImageViewAdapter;
import com.example.thriftify.view.ui.MainActivity;
import com.example.thriftify.view.ui.EditorActivity;
import com.example.thriftify.viewmodel.ProductDetailViewModel;
import com.example.thriftify.viewmodel.SingletonViewModelFactory;
import com.google.android.material.card.MaterialCardView;
import com.google.android.material.snackbar.BaseTransientBottomBar;
import com.google.android.material.snackbar.Snackbar;

import java.io.ByteArrayOutputStream;
import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import javax.inject.Inject;

import androidx.activity.result.ActivityResult;
import androidx.activity.result.ActivityResultCallback;
import androidx.activity.result.ActivityResultLauncher;
import androidx.activity.result.contract.ActivityResultContracts;
import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.annotation.RequiresApi;
import androidx.core.app.ActivityCompat;
import androidx.core.content.FileProvider;
import androidx.fragment.app.Fragment;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;

import static com.example.thriftify.utils.Constants.CAMERA_STORAGE;

@AndroidEntryPoint
public class UnbarCodeGemsFragment extends Fragment {
    private static final String TAG = "UnbarCodeGemsFragment";
    private static final int NUM_GRID_COLUMNS = 3;
    private static final int PERMISSION_REQUEST_CAMERA = 0;
    private String[] PERMISSIONS = {
            android.Manifest.permission.READ_EXTERNAL_STORAGE,
            android.Manifest.permission.CAMERA
    };

    private PermissionUtility permissionUtility;
    private Button button;
    private MaterialCardView card;
    private View mLayout;
    private ExpandableGridView gridView;
    static final int REQUEST_IMAGE_CAPTURE = 1;
    private List<Bitmap> imgURLs = new ArrayList<>();
    public static List<Bitmap> userSelection = new ArrayList<>();
    public static ActionMode mode = null;
    private GridImageViewAdapter adapter;
    public static boolean inActionMode = false;
    private String currentPhotoPath;
    private File createdImageFile = null;
    static final Uri locationForPhotos = null;
    private ProductDetailViewModel model;
    @Inject
    ProductDetailRepository repository;

    public UnbarCodeGemsFragment() {
    }
    ActivityResultLauncher<Intent> mStartForResult = registerForActivityResult(new ActivityResultContracts.StartActivityForResult(),
            new ActivityResultCallback<ActivityResult>() {

                @Override
                public void onActivityResult(final ActivityResult result) {
                    if (result.getResultCode() == Activity.RESULT_OK) {
                        Log.e(TAG, "onActivityResult: " + result.getData());
                        if (result.getData() != null) {
                            Intent intent = result.getData();
                            Intent jump = new Intent(getActivity(), EditorActivity.class);
                            jump.putExtra("camera", "nottaken");
                            jump.setData(intent.getData());
                            getActivity().startActivityForResult(jump, 100);

                        } else {
                            Intent jump = new Intent(getActivity(), EditorActivity.class);
                            jump.putExtra("camera", "imagetaken");
                            jump.setData(Uri.fromFile(createdImageFile));
                            getActivity().startActivityForResult(jump, 100);

                        }
                    }
                }
            });


    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        View view = inflater.inflate(R.layout.fragment_unbar_coded_gems, container, false);
        model = new ViewModelProvider(this, SingletonViewModelFactory.getInstance(repository)).get(ProductDetailViewModel.class);
        mLayout = view.findViewById(R.id.unbar_layout);
        this.initConfig(view);
        model.imageBitmap.observe(getActivity(), new Observer<Bitmap>() {
            @Override
            public void onChanged(Bitmap bitmap) {
                adapter.add(bitmap);
            }
        });

        return view;
    }

    /*For intial UI configuration method*/
    private void initConfig(final View view) {
        /*instantiate dropdown menu layout with dropdown data values with the help of adapter of string type*/
        ArrayAdapter<String> category_Adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, getResources().getStringArray(R.array.category));
        ArrayAdapter<String> brand_Adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, getResources().getStringArray(R.array.brand));
        ArrayAdapter<String> color_Adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, getResources().getStringArray(R.array.color));
        ArrayAdapter<String> fabric_Adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, getResources().getStringArray(R.array.fabric));
        ArrayAdapter<String> condition_Adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, getResources().getStringArray(R.array.condition));
        ArrayAdapter<String> size_Adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, getResources().getStringArray(R.array.size));
        ArrayAdapter<String> dress_Style_Adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, getResources().getStringArray(R.array.dress_Style));
        ArrayAdapter<String> dress_Length_Adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, getResources().getStringArray(R.array.dress_Length));
        ArrayAdapter<String> dress_Type_Adapter = new ArrayAdapter<>(getContext(), R.layout.dropdown_menu_popup_item, getResources().getStringArray(R.array.dress_Type));

        /*Set the adapter with auto_complete textview*/
        AutoCompleteTextView editTextFilledExposedDropdown = view.findViewById(R.id.category_filled_exposed_dropdown);
        editTextFilledExposedDropdown.setAdapter(category_Adapter);
        AutoCompleteTextView editTextFilledBrandDropdown = view.findViewById(R.id.brand_filled_exposed_dropdown);
        editTextFilledBrandDropdown.setAdapter(brand_Adapter);
        AutoCompleteTextView editTextFilledColorDropdown = view.findViewById(R.id.color_filled_exposed_dropdown);
        editTextFilledColorDropdown.setAdapter(color_Adapter);
        AutoCompleteTextView editTextFilledFabricDropdown = view.findViewById(R.id.fabric_filled_exposed_dropdown);
        editTextFilledFabricDropdown.setAdapter(fabric_Adapter);
        AutoCompleteTextView editTextFilledConditionDropdown = view.findViewById(R.id.condition_filled_exposed_dropdown);
        editTextFilledConditionDropdown.setAdapter(condition_Adapter);
        AutoCompleteTextView editTextFilledSizeDropdown = view.findViewById(R.id.size_filled_exposed_dropdown);
        editTextFilledSizeDropdown.setAdapter(size_Adapter);
        AutoCompleteTextView editTextFilledDressStyleDropdown = view.findViewById(R.id.dress_style_filled_exposed_dropdown);
        editTextFilledDressStyleDropdown.setAdapter(dress_Style_Adapter);
        AutoCompleteTextView editTextFilledDressLengthDropdown = view.findViewById(R.id.dress_length_filled_exposed_dropdown);
        editTextFilledDressLengthDropdown.setAdapter(dress_Length_Adapter);
        AutoCompleteTextView editTextFilledDressTypeDropdown = view.findViewById(R.id.dress_type_filled_exposed_dropdown);
        editTextFilledDressTypeDropdown.setAdapter(dress_Type_Adapter);
        this.button = view.findViewById(R.id.attachment_button_id);
        this.button.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(final View v) {


                if (requestPermission()) {
                    selectImages();
                } else {
                    MainActivity activity = (MainActivity) getActivity();
                    Log.e(TAG, "onClick: denied status " + SharedPreferencesUtils.getDefaults(CAMERA_STORAGE, getActivity()));
                    if (SharedPreferencesUtils.getDefaults(CAMERA_STORAGE, getActivity())) {
                        Snackbar.make(mLayout, R.string.camera_storage_access_required,
                                BaseTransientBottomBar.LENGTH_SHORT).setAction(R.string.ok, new View.OnClickListener() {
                            @RequiresApi(api = Build.VERSION_CODES.M)
                            @Override
                            public void onClick(View view) {
                                Intent intent = new Intent(Settings.ACTION_APPLICATION_DETAILS_SETTINGS);
                                Uri uri = Uri.fromParts("package", getActivity().getPackageName(), null);
                                intent.setData(uri);
                                getActivity().startActivityForResult(intent, 500);

                            }
                        }).setAnchorView(R.id.attachment_button_id).show();
                    }

                }


            }
        });
        // this.imageView = view.findViewById(R.id.view_id);

        setupImageGrid(view, imgURLs);

    }

    public void selectImages() {
        final Intent takePicture = new Intent(MediaStore.ACTION_IMAGE_CAPTURE);
        // Create the File where the photo should go
        try {
            createdImageFile = createImageFile();
        } catch (IOException ex) {
            Log.e(TAG, "selectImages: ", ex);
        }
        // Continue only if the File was successfully created
        if (createdImageFile != null) {
            Uri photoURI = FileProvider.getUriForFile(getContext(), "com.example.thriftify.fileprovider", createdImageFile);
            takePicture.putExtra(MediaStore.EXTRA_OUTPUT, photoURI);
        }

        final Intent pickPhoto = new Intent(Intent.ACTION_PICK, MediaStore.Images.Media.EXTERNAL_CONTENT_URI);
        // This says something like "Share this photo with"
        final String title = this.getResources().getString(R.string.chooser_title);
        // Create intent to show chooser
        Intent intent = Intent.createChooser(pickPhoto, title);
        intent.putExtra(Intent.EXTRA_INITIAL_INTENTS, new Intent[]{takePicture});
        // Verify the intent will resolve to at least one activity
        if (intent.resolveActivity(this.getActivity().getPackageManager()) != null) {
            this.mStartForResult.launch(intent);
        }

/*        final Intent fileManger = new Intent(Intent.ACTION_GET_CONTENT);
        fileManger.setType("image/*");
        intent.addCategory(Intent.CATEGORY_OPENABLE);
        fileManger.putExtra(Intent.EXTRA_ALLOW_MULTIPLE, true);*/
    }

    private File createImageFile() throws IOException {
        // Create an image file name
        String timeStamp = new SimpleDateFormat("yyyyMMdd_HHmmss").format(new Date());
        String imageFileName = "JPEG_" + timeStamp + "_";
        File storageDir = getActivity().getExternalFilesDir(Environment.DIRECTORY_PICTURES);
        File image = File.createTempFile(
                imageFileName,  /* prefix */
                ".jpg",         /* suffix */
                storageDir      /* directory */
        );
        // Save a file: path for use with ACTION_VIEW intents
        currentPhotoPath = image.getAbsolutePath();
        model.newFile = image;
        return image;
    }

    private void saveImage(final Bitmap finalBitmap, final File file) {
        final ByteArrayOutputStream out = new ByteArrayOutputStream();

        final byte[] bitmapData = out.toByteArray();
        //write the bytes in file
        FileOutputStream fos = null;
        try {
            fos = new FileOutputStream(file);
            fos.write(bitmapData);
            fos.flush();
            fos.close();
        } catch (IOException e) {
            Log.e(TAG, "SaveImage: ", e);
        }
    }


    private void setupImageGrid(View view, List<Bitmap> imgURLs) {
        this.gridView = (ExpandableGridView) view.findViewById(R.id.gridView);
        int gridWidth = getResources().getDisplayMetrics().widthPixels;
        int imageWidth = gridWidth / NUM_GRID_COLUMNS;
        gridView.setColumnWidth(imageWidth);
        adapter = new GridImageViewAdapter(getActivity(), imgURLs);
        gridView.setAdapter(adapter);
        gridView.setExpanded(true);
        gridView.setChoiceMode(GridView.CHOICE_MODE_MULTIPLE_MODAL);
        gridView.setMultiChoiceModeListener(new AbsListView.MultiChoiceModeListener() {
            @Override
            public void onItemCheckedStateChanged(ActionMode actionMode, int i, long l, boolean b) {
                // Here you can do something when items are selected/de-selected,
                // such as update the title in the CAB
            }

            @Override
            public boolean onCreateActionMode(ActionMode actionMode, Menu menu) {
                // Inflate the menu for the CAB
                MenuInflater inflater = actionMode.getMenuInflater();
                inflater.inflate(R.menu.context, menu);
                inActionMode = true;
                actionMode.setTitle("Select Images");
                adapter.setActionMenu(menu);
                mode = actionMode;
                return true;
            }

            @Override
            public boolean onPrepareActionMode(ActionMode actionMode, Menu menu) {
                // Here you can perform updates to the CAB due to
                // an <code><a href="/reference/android/view/ActionMode.html#invalidate()">invalidate()</a></code> request
                return false;
            }

            @Override
            public boolean onActionItemClicked(ActionMode actionMode, MenuItem menuItem) {
                // Respond to clicks on the actions in the CAB
                switch (menuItem.getItemId()) {
                    case R.id.app_bar_delete:
                        adapter.removeItems(userSelection);
                        actionMode.finish();
                        return true;
                    default:
                        return false;
                }

            }

            @Override
            public void onDestroyActionMode(ActionMode actionMode) {
                inActionMode = false;
                mode = null;
                userSelection.clear();

            }
        });

    }

    private boolean requestPermission() {
        // BEGIN_INCLUDE(startCamera)
        // Check if the Camera permission has been granted
        List<String> remainingPermissions = new ArrayList<>();
        for (String permission : PERMISSIONS) {
            if (ActivityCompat.checkSelfPermission(getActivity(), permission) != PackageManager.PERMISSION_GRANTED) {
                Log.e(TAG, "requestMultiplePermissions: " + permission);
                remainingPermissions.add(permission);
            }
        }
        if (remainingPermissions.size() > 0) {
            ActivityCompat.requestPermissions((Activity) getActivity(), remainingPermissions.toArray(new String[remainingPermissions.size()]), 101);
        } else {
            SharedPreferencesUtils.setDefaults(CAMERA_STORAGE, false, getActivity());
            return true;
        }
        return false;
    }


}