package com.example.thriftify.viewmodel;

import android.util.Log;

import com.example.thriftify.service.model.scan_model.ProductDetail;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class DetailViewModel extends ViewModel {
    private static final String TAG = "DetailViewModel";
    @Inject
    ProductDetailViewModel viewModel;
    @NonNull
    public MutableLiveData<ProductDetail> liveData = new MutableLiveData<>();

    public void getValues(){
        //this.liveData.setValue(viewModel);
    }
    @Override
    protected void onCleared() {
        super.onCleared();
        Log.d(TAG, "on cleared called");
    }
}
