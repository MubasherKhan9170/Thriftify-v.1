package com.example.thriftify.service.model.login_model;

public class LoginUserCredential {
    String username;
    String password;

    public LoginUserCredential(final String email, final String password) {
        this.username = email;
        this.password = password;
    }

    public String getEmail() {
        return this.username;
    }

    public void setEmail(final String email) {
        this.username = email;
    }

    public String getPassword() {
        return this.password;
    }

    public void setPassword(final String password) {
        this.password = password;
    }
}
