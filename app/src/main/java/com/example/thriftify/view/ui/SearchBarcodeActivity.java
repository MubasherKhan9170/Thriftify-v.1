package com.example.thriftify.view.ui;

import android.app.ProgressDialog;
import android.app.SearchManager;
import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.InputType;
import android.util.Log;
import android.view.Menu;
import android.view.MenuInflater;
import android.view.inputmethod.EditorInfo;
import android.widget.ArrayAdapter;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.example.thriftify.R;
import com.example.thriftify.databinding.ActivitySearchBarcodeBinding;
import com.example.thriftify.service.model.scan_model.ProductDetail;
import com.example.thriftify.service.respository.ProductDetailRepository;
import com.example.thriftify.utils.InternetUtils;
import com.example.thriftify.viewmodel.ProductDetailViewModel;
import com.example.thriftify.viewmodel.SingletonViewModelFactory;

import java.util.ArrayList;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.SearchView;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.LifecycleOwner;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class SearchBarcodeActivity extends AppCompatActivity {
    private static final String TAG = "SearchBarcodeActivity";
    private final Context mContext = this;
    private ArrayAdapter<String> adapter;
    private TextView view;
    ProgressDialog progressDialog;
    //instance variable
    private ProductDetailViewModel model;
    private ActivitySearchBarcodeBinding binding;
    private boolean networkStatus;
    private boolean navStatus;
    @Inject
    ProductDetailRepository repository;
    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        // setContentView(R.layout.activity_search_barcode);
        // Obtain ViewModel from ViewModelProviders
        this.model = new ViewModelProvider(this, SingletonViewModelFactory.getInstance(this.repository)).get(ProductDetailViewModel.class);
        //model = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        // Obtain binding
        this.binding = DataBindingUtil.setContentView(this,R.layout.activity_search_barcode);
        // Bind layout with ViewModel
        this.binding.setSearchModelView(this.model);

        // LiveData needs the lifecycle owner
        this.binding.setLifecycleOwner(this);
        binding.invalidateAll();
        this.progressDialog = new ProgressDialog(this);
        this.progressDialog.setMessage("Loading..."); // Setting Message
        this.progressDialog.setTitle("Please Wait"); // Setting Title
        this.progressDialog.setProgressStyle(ProgressDialog.STYLE_SPINNER);
        this.progressDialog.setIndeterminate(true);
        this.progressDialog.setCancelable(false);
        Log.e(TAG, "onCreate: ");
        view = findViewById(R.id.title_label_view);
        final ListView listView = findViewById(R.id.list);
        final ArrayList<String> list = new ArrayList<>();
        list.add("5901234123457");
        list.add("9781782808084");
        list.add("4070071967072");
        list.add("2900047030603");
        list.add("0705632085943");
        list.add("9780008319007");

        adapter = new ArrayAdapter<String>(this, android.R.layout.simple_list_item_1, list);
        listView.setAdapter(adapter);
        listView.setTextFilterEnabled(false);

        Toolbar myToolbar = findViewById(R.id.toolbar_search);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(false);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(view -> onBackPressed());
        InternetUtils.get(mContext).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    SearchBarcodeActivity.this.networkStatus = aBoolean;
                }else{
                    Toast.makeText(SearchBarcodeActivity.this.mContext, "No Internet Connection.", Toast.LENGTH_LONG).show();
                }
            }
        });

    }

    @Override
    public boolean onCreateOptionsMenu(@NonNull Menu menu) {
        MenuInflater inflater = getMenuInflater();
        inflater.inflate(R.menu.search_barcode_activity_menu, menu);
        SearchManager searchManager = (SearchManager) getSystemService(Context.SEARCH_SERVICE);
        SearchView searchView = (SearchView) menu.findItem(R.id.app_bar_search_view).getActionView();
        searchView.setQueryHint("Search ISBN Number");
        searchView.setMaxWidth(Integer.MAX_VALUE);
        searchView.setInputType(InputType.TYPE_CLASS_NUMBER);
        searchView.setImeOptions(EditorInfo.IME_ACTION_SEARCH);
        //ComponentName componentName = new ComponentName(this, ScanningDetailsGemActivity.class);
        //searchView.setSearchableInfo(searchManager.getSearchableInfo(null));
        searchView.setIconifiedByDefault(false);

        searchView.setOnQueryTextListener(new SearchView.OnQueryTextListener() {

            @Override
            public boolean onQueryTextSubmit(String query) {
                searchView.clearFocus();
                if(networkStatus){
                    SearchBarcodeActivity.this.progressDialog.show();
                    SearchBarcodeActivity.this.model.getProductValidationDetails(Long.parseLong(query), SearchBarcodeActivity.this.mContext);
                    SearchBarcodeActivity.this.runOnUiThread(new Runnable() {
                        @Override
                        public void run() {
                            //your code here
                            model.productDetailMutableLiveData.observe((LifecycleOwner) mContext, new Observer<ProductDetail>() {
                                @Override
                                public void onChanged(ProductDetail productDetail) {
                                    progressDialog.dismiss();
                                    if(model.productDetailMutableLiveData.getValue()==null){
                                        // do nothing
                                    }else{
                                        if(model.productDetailMutableLiveData.getValue().getError()!=null) {

                                            Log.e(TAG, "onChanged: with error");
                                            navStatus = true;
                                            binding.invalidateAll();
                                        }else{
                                            navStatus = false;
                                            Log.e(TAG, "onChanged: without error");
                                            binding.invalidateAll();
                                            Intent mIntent = new Intent(SearchBarcodeActivity.this, ScanningDetailsGemActivity.class);
                                            startActivity(mIntent);
                                        }
                                    }
                                }

                            });
                        /*final Handler handler = new Handler();
                        handler.postDelayed(new Runnable() {
                            @Override
                            public void run() {

                            }
                        }, 1000);*/
                        }
                    });

                }else{
                    Toast.makeText(SearchBarcodeActivity.this,"Mobile data is disabled. Connect to Wi-Fi network instead, or enable mobile data and try again", Toast.LENGTH_LONG).show();

                }

                return true;

            }

            @Override
            public boolean onQueryTextChange(String newText) {
                model.setNullifyProperty();
                binding.invalidateAll();
                return false;
            }
        });

        return true;
    }

    @Override
    protected void onResume() {
        super.onResume();
        Log.e(TAG, "onResume");


    }

    @Override
    protected void onPause() {
        super.onPause();
        Log.e(TAG, "onPause");

    }

    @Override
    protected void onStop() {
        super.onStop();
        Log.e(TAG, "onStop");
    }

}
