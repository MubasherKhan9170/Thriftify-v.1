package com.example.thriftify.service.model.login_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

/**
 * Authentication result : success (user details) or error message.
 */
public class LoginResult {
    @SerializedName("success")
    @Expose
    private String success = null;

    @SerializedName("error")
    @Expose
    private String error = null;

    public String getSuccess() {
        return this.success;
    }

    public String getLoginError() {
        return this.error;
    }

    public void setSuccess(final String success) {
        this.success = success;
    }

    public void setError(final String error) {
        this.error = error;
    }
}