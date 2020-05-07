package com.example.demo.repository.dao.order;

public class OrderResponse {
    private String transactionId;
    private boolean userExists;
    private boolean isEmailVerified;

    public OrderResponse() {
    }

    public OrderResponse(String transactionId, boolean userExists, boolean isEmailVerified) {
        this.transactionId = transactionId;
        this.userExists = userExists;
        this.isEmailVerified = isEmailVerified;
    }

    public String getTransactionId() {
        return transactionId;
    }

    public void setTransactionId(String transactionId) {
        this.transactionId = transactionId;
    }

    public boolean isEmailVerified() {
        return isEmailVerified;
    }

    public void setEmailVerified(boolean emailVerified) {
        isEmailVerified = emailVerified;
    }

    public boolean isUserExists() {
        return userExists;
    }

    public void setUserExists(boolean userExists) {
        this.userExists = userExists;
    }
}
