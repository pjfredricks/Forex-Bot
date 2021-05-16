package com.forexbot.api.dao.order;

import lombok.Data;

@Data
public class CalculateResponse {
    private String customerId;
    private double forexTotal;
    private double gst;
    private double discountAmount;
    private int salesTotal;
}
