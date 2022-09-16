package com.example.thriftify.service.respository;

import android.content.Context;
import android.util.Log;

import com.example.thriftify.service.Webservice;
import com.example.thriftify.service.model.login_model.LoggedInUser;
import com.example.thriftify.service.model.login_model.LoginDataSource;
import com.example.thriftify.service.model.login_model.LoginError;
import com.example.thriftify.service.model.login_model.LoginResult;
import com.example.thriftify.service.model.login_model.LoginUserCredential;
import com.example.thriftify.utils.SharedPreferencesUtils;
import com.example.thriftify.view.callbacks.LoginCallback;
import com.google.gson.Gson;

import java.io.IOException;

import javax.inject.Inject;

import retrofit2.Call;
import retrofit2.Callback;
import retrofit2.Response;

/**
 * Class that requests authentication and user information from the remote data source and
 * maintains an in-memory cache of login status and user credentials information.
 */
public class LoginRepository {
    private static final String TAG = "LoginRepository";

    private final Webservice webservice;

    private LoginDataSource dataSource;
    private LoginResult result = new LoginResult();

    // If user credentials will be cached in local storage, it is recommended it be encrypted
    // @see https://developer.android.com/training/articles/keystore
    private LoggedInUser user = null;

    @Inject
    public LoginRepository( Webservice webservice) {
        this.webservice = webservice;
    }

    public boolean isLoggedIn() {
        return user != null;
    }

    public void logout() {
        user = null;
        dataSource.logout();
    }


    public void login(Context context, LoginUserCredential userCredential, LoginCallback callback){
       this.webservice.getUserToken(userCredential).enqueue(new Callback<LoggedInUser>() {
           @Override
           public void onResponse(Call<LoggedInUser> call, Response<LoggedInUser> response) {
               if(response.isSuccessful()){
                   //result.setSuccess(new Gson().fromJson(response.body().toString(), LoggedInUser.class));
                   result.setSuccess("You have Successfully logged in!!!");
                   result.setError(null);
                   setLoggedInUser(context, response.body().getToken());
                   callback.onSuccess(result);
                   Log.e(TAG, "onSuccess: "+ result.getSuccess());
               }else{
                   result.setSuccess(null);
                   try {
                      // Gson gson = new Gson().fromJson(response.errorBody().string(), String.class);
                       LoginError errorResponse = new Gson().fromJson(response.errorBody().string(), LoginError.class);
                       result.setError(errorResponse.getError());
                   } catch (IOException e) {
                       e.printStackTrace();
                   }
                   callback.onError(result);
                   Log.e(TAG, "onError: "+ result.getLoginError());
               }
           }

           @Override
           public void onFailure(Call<LoggedInUser> call, Throwable t) {
               Log.e(TAG, "onFailure: ", t);

               result.setSuccess("3543534345354");
               result.setError("Hand Shaking Issue");
               setLoggedInUser(context, result.getSuccess());
               callback.onError(result);
           }
       });
    }


    private void setLoggedInUser(Context context, String token) {
        // If user credentials will be cached in local storage, it is recommended it be encrypted
        // @see https://developer.android.com/training/articles/keystore
        //BuildConfig.ACCESS_TOKEN = user;
        Log.i(TAG, "setLoggedInUser before : "+ SharedPreferencesUtils.get(context).getTokenSharedPreferences());
        SharedPreferencesUtils.get(context).setTokenSharedPreferences(token);
        Log.i(TAG, "setLoggedInUser: after "+ SharedPreferencesUtils.get(context).getTokenSharedPreferences());
    }

}
