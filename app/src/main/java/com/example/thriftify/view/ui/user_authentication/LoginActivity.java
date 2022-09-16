package com.example.thriftify.view.ui.user_authentication;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.text.Editable;
import android.text.TextWatcher;
import android.util.Log;
import android.view.View;
import android.widget.Button;
import android.widget.ProgressBar;
import android.widget.Toast;

import com.example.thriftify.R;
import com.example.thriftify.service.model.login_model.LoginResult;
import com.example.thriftify.utils.InternetUtils;
import com.example.thriftify.view.ui.MainActivity;
import com.example.thriftify.viewmodel.LoginViewModel;
import com.google.android.material.textfield.TextInputLayout;

import androidx.annotation.Nullable;
import androidx.appcompat.app.AppCompatActivity;
import androidx.lifecycle.Observer;
import androidx.lifecycle.ViewModelProvider;
import dagger.hilt.android.AndroidEntryPoint;

@AndroidEntryPoint
public class LoginActivity extends AppCompatActivity {
    // tag
    private static final String TAG = "LoginActivity";

    // variables
    private Context mContext = this;
    private boolean networkStatus;
    private LoginViewModel loginViewModel;

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_login);
        // get the viewModel class instance
        this.loginViewModel = new ViewModelProvider(this).get(LoginViewModel.class);

        // get views by using view ids
        final TextInputLayout usernameEditText = findViewById(R.id.email_textInputLayout);
        final TextInputLayout passwordEditText = findViewById(R.id.password_textInputLayout);
        final Button loginButton = findViewById(R.id.login);
        final ProgressBar loadingProgressBar = findViewById(R.id.loading);

        // get the status of Network connectivity
        InternetUtils.get(mContext).observe(this, new Observer<Boolean>() {
            @Override
            public void onChanged(Boolean aBoolean) {
                if(aBoolean){
                    LoginActivity.this.networkStatus = aBoolean;
                }else{
                    LoginActivity.this.networkStatus = aBoolean;
                    Toast.makeText(LoginActivity.this.mContext, "No Internet Connection.", Toast.LENGTH_LONG).show();
                }
            }
        });

        // get the states from login form
        loginViewModel.getLoginFormState().observe(this, new Observer<LoginFormState>() {
            @Override
            public void onChanged(@Nullable LoginFormState loginFormState) {
                if (loginFormState == null) {
                    return;
                }
                loginButton.setEnabled(loginFormState.isDataValid());
                if (loginFormState.getUsernameError() != null) {
                    usernameEditText.setError(getString(loginFormState.getUsernameError()));
                }else{
                    usernameEditText.setError(null);
                }
                if (loginFormState.getPasswordError() != null) {
                   // passwordEditText.setError(getString(loginFormState.getPasswordError()));
                }
                else{
                    passwordEditText.setError(null);
                }
            }
        });

        // get the login result by setting up the observer
        loginViewModel.getLoginResult().observe(this, new Observer<LoginResult>() {

            @Override
            public void onChanged(@Nullable LoginResult loginResult) {
                Log.e(TAG, "onChanged: "+ loginResult.getSuccess() );
                if (loginResult == null) {
                    Log.e(TAG, "onChanged: null");
                    return;
                }
                if (loginResult.getLoginError()!= null) {
                    Log.e(TAG, "onError: "+ loginResult.getLoginError());
                    loadingProgressBar.setVisibility(View.GONE);
                    showLoginFailed(loginResult.getLoginError());
                }
                if (loginResult.getSuccess()!= null) {
                    Log.e(TAG, "onChangedSuccess: "+ loginResult.getSuccess());
                    loadingProgressBar.setVisibility(View.GONE);
                    updateUiWithUser(loginResult.getSuccess());
                    Intent i = new Intent(LoginActivity.this, MainActivity.class);
                    LoginActivity.this.startActivity(i);
                    LoginActivity.this.finish();
                }

            }
        });

        // set up the textWatcher listener to listen the change of into the Edit field
        TextWatcher afterTextChangedListener = new TextWatcher() {
            @Override
            public void beforeTextChanged(CharSequence s, int start, int count, int after) {
                // ignore
            }

            @Override
            public void onTextChanged(CharSequence s, int start, int before, int count) {
                // ignore
            }

            @Override
            public void afterTextChanged(Editable s) {
                loginViewModel.loginDataChanged(usernameEditText.getEditText().getText().toString(),
                        passwordEditText.getEditText().getText().toString());
            }
        };

        // add the listener with the Edit field
        usernameEditText.getEditText().addTextChangedListener(afterTextChangedListener);
        passwordEditText.getEditText().addTextChangedListener(afterTextChangedListener);

        // attach the onClick listener to login button
        loginButton.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if(networkStatus){
                    loadingProgressBar.setVisibility(View.VISIBLE);
                    loginViewModel.login(usernameEditText.getEditText().getText().toString(),
                            passwordEditText.getEditText().getText().toString());
                }else{
                    Toast.makeText(LoginActivity.this,"Mobile data is disabled. Connect to Wi-Fi network instead, or enable mobile data and try again", Toast.LENGTH_LONG).show();
                }
            }
        });
    }
    // call this when successful authentication by the server acceptance
    private void updateUiWithUser(String token) {
        String welcome = token;
        // TODO : initiate successful logged in experience
        Toast.makeText(getApplicationContext(), welcome, Toast.LENGTH_LONG).show();
    }

    //call this when not remember by the server
    private void showLoginFailed(@Nullable String errorString) {
        // TODO : unsccessful logged in experience
        Toast.makeText(getApplicationContext(), errorString, Toast.LENGTH_SHORT).show();
    }

    @Override
    public void onBackPressed() {
        super.onBackPressed();
    }
}