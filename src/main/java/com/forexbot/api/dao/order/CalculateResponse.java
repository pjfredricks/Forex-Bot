package com.forexbot.api.dao.order;

public class CalculateResponse {

    private String customerId;
    private double forexTotal;
    private double gst;
    private double discountAmount;
    private int salesTotal;

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
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

    public int getSalesTotal() {
        return salesTotal;
    }

    public void setSalesTotal(int  salesTotal) {
        this.salesTotal = salesTotal;
    }
}