package com.example.thriftify.viewmodel;

import android.app.Application;
import android.content.Context;
import android.util.Patterns;

import com.example.thriftify.R;
import com.example.thriftify.service.model.login_model.LoginResult;
import com.example.thriftify.service.model.login_model.LoginUserCredential;
import com.example.thriftify.service.respository.LoginRepository;
import com.example.thriftify.view.callbacks.LoginCallback;
import com.example.thriftify.view.ui.user_authentication.LoginFormState;

import javax.inject.Inject;

import androidx.annotation.NonNull;
import androidx.lifecycle.AndroidViewModel;
import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;
import dagger.hilt.android.lifecycle.HiltViewModel;

@HiltViewModel
public class LoginViewModel extends AndroidViewModel {
    private static final String TAG = "LoginViewModel";

private Context context;
    private MutableLiveData<LoginFormState> loginFormState = new MutableLiveData<>();
    private MutableLiveData<LoginResult> loginResult = new MutableLiveData<>();
    private LoginRepository loginRepository;

   @Inject
    public LoginViewModel(@NonNull Application application, LoginRepository loginRepository) {
        super(application);
        this.loginRepository = loginRepository;
        context = application;
    }

    public LiveData<LoginFormState> getLoginFormState() {
        return loginFormState;
    }
    @NonNull
    public LiveData<LoginResult>  getLoginResult() {

        return loginResult;
    }

    public void login(String username, String password) {
       loginRepository.login(context, new LoginUserCredential(username, password), new LoginCallback() {
           @Override
           public void onSuccess(LoginResult response) {
               loginResult.setValue(response);
           }

           @Override
           public void onError(LoginResult error) {
               loginResult.setValue(error);

           }
       });


    }

    public void loginDataChanged(String username, String password) {

        if (!isUserNameValid(username)) {
            loginFormState.setValue(new LoginFormState(R.string.invalid_email, null));
        } else if (!isPasswordValid(password)) {
            loginFormState.setValue(new LoginFormState(null, R.string.invalid_password));
        } else {
            loginFormState.setValue(new LoginFormState(true));
        }
    }

    // A placeholder username validation check
    private boolean isUserNameValid(String username) {


        if(username.contains("@") && !username.trim().isEmpty()) {
            return Patterns.EMAIL_ADDRESS.matcher(username).matches();
        }else{
            return false;
        }
        }



    // A placeholder password validation check
    private boolean isPasswordValid(String password) {
        return password != null && password.trim().length() > 5;
    }

    @Override
    protected void onCleared() {
        super.onCleared();
    }
}