package com.forexbot.api.dao.order;

import lombok.Data;

@Data
public class CalculateRequest {
    private String customerId;
    private String countryCode;
    private int orderType;
    private int forexAmount;
    private String couponCode;
}
