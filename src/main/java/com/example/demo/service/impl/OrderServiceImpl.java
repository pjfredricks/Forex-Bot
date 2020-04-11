package com.example.demo.service.impl;

import com.example.demo.repository.dao.Order.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;

    public OrderServiceImpl(OrderRepository repository) {
        this.orderRepository = repository;
    }

    @Override
    public Order getOrderByUserIdOrTrackingNumberOrCouponCode(String userId,
                                                              String trackingNumber,
                                                              String couponCode) {
        return orderRepository.getOrderByUserIdOrTrackingNumberOrCouponCode(userId, trackingNumber, couponCode);
    }

    @Override
    public Order placeOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public boolean prepareOrder(Order order) {
        calculateOrder(order);
        return false;
    }

    @Override
    public Order calculateOrder(Object object) {
        return null;
    }
}
