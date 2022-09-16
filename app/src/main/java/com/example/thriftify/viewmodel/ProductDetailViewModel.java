package com.example.thriftify.viewmodel;

import android.content.Context;
import android.graphics.Bitmap;
import android.util.Log;

import com.example.thriftify.service.model.scan_model.ProductDetail;
import com.example.thriftify.service.respository.ProductDetailRepository;

import java.io.File;
import java.util.ArrayList;
import java.util.List;

import androidx.annotation.NonNull;
import androidx.annotation.Nullable;
import androidx.lifecycle.MutableLiveData;
import androidx.lifecycle.ViewModel;

public class ProductDetailViewModel extends ViewModel {
    private static final String TAG = "ProductDetailViewModel";
    private static ProductDetailViewModel sInstance = null;
    private static ProductDetailRepository productDetailRepository = null;
    public  MutableLiveData<Bitmap>  imageBitmap= new MutableLiveData<>();
    public File newFile;
    public  static List<File> filesList = new ArrayList<File>();
    //binding with scanning barcode fragment views
    @Nullable
    public MutableLiveData<ProductDetail> productDetailMutableLiveData = null;
    @Nullable
    public MutableLiveData<String> toastMessage = new MutableLiveData<>();

    //@ViewModelInject
    private ProductDetailViewModel(@NonNull ProductDetailRepository productDetailRepository) {
            this.productDetailRepository = productDetailRepository;
        }

    public static ProductDetailViewModel getInstance(ProductDetailRepository repository) {
        synchronized (ProductDetailViewModel.class){
            if(ProductDetailViewModel.sInstance == null){
                ProductDetailViewModel.sInstance = new ProductDetailViewModel(repository);
                Log.e(TAG, "ProductDetailViewModelInstance: "+ ProductDetailViewModel.sInstance);
            }
        }
        return ProductDetailViewModel.sInstance;
    }


    public void getProductValidationDetails(long barcode, Context context) {
        if (this.productDetailMutableLiveData == null) {
            this.productDetailMutableLiveData = new MutableLiveData<>();
            Log.e(TAG, "Values: "+ productDetailMutableLiveData);
        }
        this.productDetailMutableLiveData = this.productDetailRepository.getProductDetailResponseResult(barcode, 9999, 9999, "67d829bf61dc5f87a73fd814e2c9f629", context);
    }

public final void setNullifyProperty(){
    if(this.productDetailMutableLiveData != null) {
        if(this.productDetailMutableLiveData.getValue()!= null){
            productDetailMutableLiveData.setValue(null);
        }
    }
}
    @Override
    protected void onCleared() {
        super.onCleared();
        Log.i(TAG, "on cleared called");
    }

    @Override
    public String toString() {
        return "ProductDetailViewModel{" +
                "productDetailMutableLiveData=" + productDetailMutableLiveData +
                ", toastMessage=" + toastMessage +
                '}';
    }
}