package com.example.demo.service;

import com.example.demo.repository.dao.Order.CalculateResponse;
import com.example.demo.repository.dao.Order.Order;
import com.example.demo.repository.dao.Order.CalculateRequest;

public interface OrderService {

    Order getOrderByUserIdOrTrackingNumberOrCouponCode(String userId, String trackingNumber, String couponCode);

    String placeOrder(Order order);

    Order updateOrder(Order order);

    CalculateResponse calculateOrder(CalculateRequest orderRequest);
}
