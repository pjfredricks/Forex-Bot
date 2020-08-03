package com.forexbot.api.service;

import com.forexbot.api.dao.order.CalculateRequest;
import com.forexbot.api.dao.order.CalculateResponse;
import com.forexbot.api.dao.order.Order;
import com.forexbot.api.dao.order.OrderResponse;

import java.util.List;

public interface OrderService {

    List<Order> getAllOrders();

    Order getOrderByCustomerIdOrTrackingIdOrCouponCode(String customerId, String trackingId, String couponCode);

    OrderResponse placeOrder(CalculateRequest request);

    Order updateOrder(Order order);

    CalculateResponse calculateOrder(CalculateRequest orderRequest);
}
