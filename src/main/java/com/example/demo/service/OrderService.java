package com.example.demo.service;

import com.example.demo.repository.dao.Order.Order;

public interface OrderService {

    Order getOrderByUserIdOrTrackingNumberOrCouponCode(String userId, String trackingNumber, String couponCode);

    Order placeOrder(Order order);

    Order updateOrder(Order order);

    Order calculateOrder(Object object);

    boolean prepareOrder(Order order);
}
