package com.forexbot.api.dao.backoffice.userdata;

public class BackOfficeLoginRequest {
    private String emailId;
    private String password;

    public BackOfficeLoginRequest() {
        super();
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }
}
