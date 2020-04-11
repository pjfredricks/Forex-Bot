package com.example.demo.repository;

import com.example.demo.repository.dao.Order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order getOrderByUserIdOrTrackingNumberOrCouponCode(String userId, String trackingNumber, String couponCode);

    Order save(Order order);
}