package com.forexbot.api.dao.admin;

public class VendorResponse {
    private String userName;
    private String agentId;

    public VendorResponse() {
    }

    public VendorResponse(String userName, String agentId) {
        this.userName = userName;
        this.agentId = agentId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getAgentId() {
        return agentId;
    }

    public void setAgentId(String agentId) {
        this.agentId = agentId;
    }
}
