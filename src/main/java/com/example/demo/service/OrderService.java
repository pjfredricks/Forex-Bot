package com.example.demo.service;

import com.example.demo.repository.dao.order.CalculateResponse;
import com.example.demo.repository.dao.order.Order;
import com.example.demo.repository.dao.order.CalculateRequest;
import com.example.demo.repository.dao.order.OrderResponse;

public interface OrderService {

    Order getOrderByUserIdOrTrackingNumberOrCouponCode(String userId, String trackingNumber, String couponCode);

    OrderResponse placeOrder(CalculateRequest request);

    Order updateOrder(Order order);

    CalculateResponse calculateOrder(CalculateRequest orderRequest);
}
