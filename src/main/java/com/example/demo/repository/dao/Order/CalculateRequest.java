package com.example.demo.repository.dao.Order;

public class CalculateRequest {

    private String userId;
    private String countryCode;
    private int orderType;
    private int forexAmount;
    private String couponCode;

    public String getUserId() {
        return userId;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public int getOrderType() {
        return orderType;
    }

    public int getForexAmount() {
        return forexAmount;
    }

    public String getCouponCode() {
        return couponCode;
    }
}