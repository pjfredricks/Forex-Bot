package com.example.demo.repository.dao.Order;

public class CalculateResponse {

    private String userId;
    private double forexTotal;
    private double gst;
    private double discountAmount;
    private double salesTotal;

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public double getForexTotal() {
        return forexTotal;
    }

    public void setForexTotal(double  forexTotal) {
        this.forexTotal = forexTotal;
    }

    public double getGst() {
        return gst;
    }

    public void setGst(double gst) {
        this.gst = gst;
    }

    public double getDiscountAmount() {
        return discountAmount;
    }

    public void setDiscountAmount(double  discountAmount) {
        this.discountAmount = discountAmount;
    }

    public double getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(double  salesTotal) {
        this.salesTotal = salesTotal;
    }
}