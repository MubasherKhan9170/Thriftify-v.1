package com.example.thriftify.view.ui;

import android.app.SearchManager;
import android.content.Intent;
import android.os.Bundle;
import android.widget.Toast;

import com.example.thriftify.R;
import com.example.thriftify.databinding.ActivityScanningDetailsGemBinding;
import com.example.thriftify.service.respository.ProductDetailRepository;
import com.example.thriftify.utils.InternetUtils;
import com.example.thriftify.viewmodel.ProductDetailViewModel;
import com.example.thriftify.viewmodel.SingletonViewModelFactory;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.appcompat.widget.Toolbar;
import androidx.databinding.DataBindingUtil;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class ScanningDetailsGemActivity extends AppCompatActivity {
    private static final String TAG = "ScanningDetailsGemActiv";
    //instance variable
    private ProductDetailViewModel model;
    private boolean networkStatus = false;
    @Inject
    ProductDetailRepository repository;


    @Override
    protected void onCreate(@NonNull Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //setContentView(R.layout.activity_scanning_details_gem);
        // Obtain ViewModel from ViewModelProviders
        model = new ViewModelProvider(this, SingletonViewModelFactory.getInstance(repository)).get(ProductDetailViewModel.class);
        //model = new ViewModelProvider(this).get(ProductDetailViewModel.class);
        // Obtain binding
        final ActivityScanningDetailsGemBinding detailsGemBinding = DataBindingUtil.setContentView(this,R.layout.activity_scanning_details_gem);
        // Bind layout with ViewModel
        detailsGemBinding.setViewModel(model);

        // LiveData needs the lifecycle owner
        detailsGemBinding.setLifecycleOwner(this);

        InternetUtils.get(this).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(final Boolean aBoolean) {
                if(aBoolean){
                    // Toast.makeText(ScanningDetailsGemActivity.this,"Network is availble", Toast.LENGTH_SHORT).show();
                    networkStatus = aBoolean;

                }else{
                    Toast.makeText(ScanningDetailsGemActivity.this, "Network is unavailable, Please turn on", Toast.LENGTH_LONG).show();
                    networkStatus = aBoolean;
                }
            }
        });

        Toolbar myToolbar = findViewById(R.id.toolbar_scanning_details);
        setSupportActionBar(myToolbar);
        getSupportActionBar().setDisplayShowTitleEnabled(true);
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        myToolbar.setNavigationOnClickListener(view -> onBackPressed());
        handleIntent(getIntent());

/*        model.productDetailMutableLiveData.observe(this, new Observer<ProductDetail>() {
            @Override
            public void onChanged(ProductDetail productDetail) {
                Log.e(TAG, "onChanged: Hua hai" );
            }
        });*/

        //detailsGemBinding.invalidateAll();
    }

    @Override
    protected void onNewIntent(@NonNull Intent intent) {
        super.onNewIntent(intent);
        handleIntent(intent);
    }

    private void handleIntent(Intent intent) {
        long barcode;
        if (Intent.ACTION_SEARCH.equals(intent.getAction())) {
            String query = intent.getStringExtra(SearchManager.QUERY);
            barcode = Long.parseLong(query);
            //use the query to search your data somehow
            //this.model.getDetails(barcode, this);
        }
        else{
            // Log.e(TAG, intent.getSerializableExtra("details").toString());
            // model.getDetails(barcode, this);
        }
    }
}
