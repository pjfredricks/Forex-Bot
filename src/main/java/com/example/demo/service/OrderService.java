package com.example.demo.service;

import com.example.demo.repository.dao.order.CalculateResponse;
import com.example.demo.repository.dao.order.Order;
import com.example.demo.repository.dao.order.CalculateRequest;

public interface OrderService {

    Order getOrderByUserIdOrTrackingNumberOrCouponCode(String userId, String trackingNumber, String couponCode);

    String placeOrder(CalculateRequest request) throws IllegalAccessException;

    Order updateOrder(Order order);

    CalculateResponse calculateOrder(CalculateRequest orderRequest);
}
