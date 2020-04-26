package com.example.demo.repository.dao.userdata;

public class ResetPasswordRequest {
    private String userId;
    private String password;

    public ResetPasswordRequest() {
        super();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }
}
