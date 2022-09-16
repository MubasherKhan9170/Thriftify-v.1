package com.example.thriftify.service.respository;

import android.content.Context;
import android.util.Log;
import android.widget.Toast;

import com.example.thriftify.service.Webservice;
import com.example.thriftify.service.model.scan_model.ProductDetail;
import com.example.thriftify.service.model.ship_model.PendingOrdersModel;
import com.example.thriftify.service.model.ship_model.ShippedOrdersModel;
import com.google.gson.JsonSyntaxException;

import java.net.SocketTimeoutException;
import java.net.UnknownHostException;
import java.util.ArrayList;
import java.util.List;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.MutableLiveData;
import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

public class ProductDetailRepository{
    private static final String TAG = "ProductDetailRepository";
    private final Webservice webservice;


    @Inject
    public ProductDetailRepository( Webservice webservice) {
        this.webservice = webservice;
    }



   @NonNull
    public MutableLiveData<ProductDetail> getProductDetailResponseResult(long barcode, long id, long outletId, @NonNull String apiKey, Context context){
       MutableLiveData<ProductDetail> mProductDetailMutableLiveData = new MutableLiveData<>();
       this.webservice.getProductDetail(barcode, id, outletId, apiKey).enqueue(new Callback<ProductDetail>() {
            @Override
            public void onResponse(final Call<ProductDetail> call, final Response<ProductDetail> response) {
                if(response.isSuccessful()){
                        Log.e(TAG, "success "  + String.valueOf(response.code()));
                        mProductDetailMutableLiveData.setValue(response.body());
                } else {
                    Log.e(TAG, "error "+ String.valueOf(response.code()));
                    /*String error = null;
                    try {
                        error = response.errorBody().string();
                    } catch (IOException e) {
                        e.printStackTrace();
                    }
                    Log.e(TAG, "error body "  + error);*/
                    if(response.code() == 500){
                        Log.e(TAG, "Found Internal Server Error");
                        mProductDetailMutableLiveData.setValue(null);
                        call.cancel();
                        Toast.makeText(context, "Found Internal Server Error", Toast.LENGTH_LONG).show();
                    }
                }
            }

            @Override
            public void onFailure(final Call<ProductDetail> call, final Throwable t) {
                call.cancel();
                mProductDetailMutableLiveData.setValue(null);
                if(t instanceof UnknownHostException){
                    Toast.makeText(context, "Failed to make a network request call. Check Your phone's Internet Connection and try again", Toast.LENGTH_LONG).show();
                }else if(t instanceof SocketTimeoutException){
                    Toast.makeText(context, "Timeout.Your phone Internet Connection is slow down and try again", Toast.LENGTH_LONG).show();
                }else if(t instanceof JsonSyntaxException){
                    Toast.makeText(context, "Couldn't parse the json response.", Toast.LENGTH_LONG).show();
                }else{
                    Toast.makeText(context, t.getMessage(), Toast.LENGTH_LONG).show();
                    Log.e(TAG, "getProductDetailResponseResultFailureCause: "+ t);
                }
                return;
                }


       });

        return mProductDetailMutableLiveData;
    }

    public MutableLiveData<List<PendingOrdersModel>> getPendingOrdersList (){
        MutableLiveData<List<PendingOrdersModel>> data = new MutableLiveData<>();
        List<PendingOrdersModel> list = new ArrayList<>();

        PendingOrdersModel item_1 = new PendingOrdersModel("Atomic Habits: The life-changing million copy bestseller");
        PendingOrdersModel item_2= new PendingOrdersModel("Pending Product B");
        PendingOrdersModel item_3 = new PendingOrdersModel("Pending Product C");
        PendingOrdersModel item_4 = new PendingOrdersModel("Pending Product D");

        list.add(item_1);
        list.add(item_2);
        list.add(item_3);
        list.add(item_4);

        data.setValue(list);
        return data;
    }

    public MutableLiveData<List<ShippedOrdersModel>> getShippedOrdersList (){
        MutableLiveData<List<ShippedOrdersModel>> data = new MutableLiveData<>();
        List<ShippedOrdersModel> list = new ArrayList<>();

        ShippedOrdersModel item_1 = new ShippedOrdersModel("Atomic Habits: The life-changing million copy bestseller");
        ShippedOrdersModel item_2 = new ShippedOrdersModel("Shipped Order A");
        ShippedOrdersModel item_3 = new ShippedOrdersModel("Shipped Order B");
        ShippedOrdersModel item_4 = new ShippedOrdersModel("Shipped Order C");
        list.add(item_1);
        list.add(item_2);
        list.add(item_3);
        list.add(item_4);
        data.setValue(list);
        return data;
    }


}
