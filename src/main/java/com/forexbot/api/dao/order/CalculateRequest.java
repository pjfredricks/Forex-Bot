package com.forexbot.api.dao.order;

public class CalculateRequest {

    private String customerId;
    private String countryCode;
    private int orderType;
    private int forexAmount;
    private String couponCode;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public int getOrderType() {
        return orderType;
    }

    public void setOrderType(int orderType) {
        this.orderType = orderType;
    }

    public int getForexAmount() {
        return forexAmount;
    }


    public void setForexAmount(int forexAmount) {
        this.forexAmount = forexAmount;
    }

    public String getCouponCode() {
        return couponCode;
    }

    public void setCouponCode(String couponCode) {
        this.couponCode = couponCode;
    }
}