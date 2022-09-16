package com.example.thriftify.service.model.login_model;

import com.google.gson.annotations.Expose;
import com.google.gson.annotations.SerializedName;

import java.io.Serializable;

public class LoginError implements Serializable {

        @SerializedName("error")
        @Expose
        private String error;

        public LoginError(String error) {
            this.error = error;
        }

        public String getError() {
            return error;
        }

        public void setError(String token) {
            this.error = error;
        }


}
