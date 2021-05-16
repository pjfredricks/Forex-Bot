package com.forexbot.api.repository;

import com.forexbot.api.dao.order.Order;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface OrderRepository extends JpaRepository<Order, Integer> {

    Order getOrderByCustomerIdOrTrackingIdOrCouponCode(UUID customerId, String trackingId, String couponCode);

    Order getOrderByTrackingId(String trackingId);
}
