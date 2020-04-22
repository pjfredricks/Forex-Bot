package com.example.demo.repository.dao.order;

public class CalculateRequest {

    private String userId;
    private String countryCode;
    private int orderType;
    private int forexAmount;
    private String couponCode;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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