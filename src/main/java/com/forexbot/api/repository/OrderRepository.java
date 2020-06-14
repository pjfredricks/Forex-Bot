package com.forexbot.api.repository;

import com.forexbot.api.dao.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order getOrderByUserIdOrTrackingNumberOrCouponCode(String userId, String trackingNumber, String couponCode);

    Order getOrderByTrackingNumber(String trackingNumber);

    Order save(Order order);

    List<Order> findAll();
}