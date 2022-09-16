package com.example.thriftify.service;

import com.example.thriftify.service.model.login_model.LoggedInUser;
import com.example.thriftify.service.model.login_model.LoginUserCredential;
import com.example.thriftify.service.model.scan_model.ProductDetail;

import androidx.annotation.NonNull;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.GET;
import retrofit2.http.Headers;
import retrofit2.http.POST;
import retrofit2.http.Path;
import retrofit2.http.Query;
// Single Abstract Method Interface
//@FunctionalInterface
public interface Webservice {

    // Authorization Endpoint(Get user Token)
    @NonNull
    @Headers({
            "Accept: application/json",
            "API-Key: dxqqpCWreoZAZMagkAH3T9Tm7MHyiC"
    })
    @POST("/api/v1/auth/token")
    Call<LoggedInUser>getUserToken(@Body LoginUserCredential user);


    // get product details endpoint
   // @GET("/api/users/{id}")
    @NonNull
    @GET("https://api.thriftify.com/v1/getproduct/barcode/{barcode}/userId/{user_id}/currencyCode/EUR/outletId/{outlet_id}")
    Call<ProductDetail>getProductDetail(@Path("barcode") long barcode, @Path("user_id") long userId, @Path("outlet_id") long outletId, @Query("apiKey") @NonNull String api_key);



}
