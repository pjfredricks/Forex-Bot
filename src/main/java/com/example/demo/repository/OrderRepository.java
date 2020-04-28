package com.example.demo.repository;

import com.example.demo.repository.dao.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order getOrderByUserIdOrTrackingNumberOrCouponCode(String userId, String trackingNumber, String couponCode);

    Order save(Order order);

    List<Order> findAll();
}