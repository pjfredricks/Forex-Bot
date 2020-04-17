package com.example.demo.repository.dao.Order;

public class CalculateRequest {

    private String userId;
    private String countryCode;
    private OrderType type;
    private int forexAmount;
    private String promoCode;

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

    public OrderType getType() {
        return type;
    }

    public void setType(OrderType type) {
        this.type = type;
    }

    public int getForexAmount() {
        return forexAmount;
    }

    public void setForexAmount(int forexAmount) {
        this.forexAmount = forexAmount;
    }

    public String getPromoCode() {
        return promoCode;
    }

    public void setPromoCode(String promoCode) {
        this.promoCode = promoCode;
    }
}