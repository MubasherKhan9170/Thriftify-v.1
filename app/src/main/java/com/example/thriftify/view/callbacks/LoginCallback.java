package com.example.thriftify.view.callbacks;

import com.example.thriftify.service.model.login_model.LoginResult;

public interface LoginCallback {
    void onSuccess(LoginResult response);
    void onError(LoginResult error);
}
