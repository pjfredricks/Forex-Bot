package com.forexbot.api.dao.admin;

public class BackOfficeLoginResponse {
    private String userName;
    private String vendorAgentId;

    public BackOfficeLoginResponse() {
    }

    public BackOfficeLoginResponse(String userName, String vendorAgentId) {
        this.userName = userName;
        this.vendorAgentId = vendorAgentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getVendorAgentId() {
        return vendorAgentId;
    }

    public void setVendorAgentId(String vendorAgentId) {
        this.vendorAgentId = vendorAgentId;
    }
}
