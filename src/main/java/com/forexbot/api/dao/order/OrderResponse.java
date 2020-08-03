package com.forexbot.api.dao.order;

public class OrderResponse {
    private String transactionId;
    private boolean customerExists;
    private boolean isEmailVerified;

    public OrderResponse() {
        super();
        // Set default values
        transactionId = null;
        customerExists = false;
        isEmailVerified = false;
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

    public boolean isCustomerExists() {
        return customerExists;
    }

    public void setCustomerExists(boolean customerExists) {
        this.customerExists = customerExists;
    }
}
