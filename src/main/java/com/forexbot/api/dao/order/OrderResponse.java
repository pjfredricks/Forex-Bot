package com.forexbot.api.dao.order;

import lombok.Data;

@Data
public class OrderResponse {
    private String transactionId = null;
    private boolean customerExists = false;
    private boolean isEmailVerified = false;
}
