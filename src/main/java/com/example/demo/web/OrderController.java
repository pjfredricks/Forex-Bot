package com.example.demo.web;

import com.example.demo.repository.dao.Order.Order;
import com.example.demo.repository.dao.ResponseWrapper;
import com.example.demo.service.OrderService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class OrderController {

    private OrderService orderService;

    public OrderController(OrderService orderService) {
        this.orderService = orderService;
    }

    @GetMapping(path = "/order/userId/{userId}")
    public ResponseEntity<ResponseWrapper> getOrderByUserId(@RequestParam String userId) {
        return new ResponseEntity<>(new ResponseWrapper(
                "SUCCESS",
                "Order Details fetched for userId: " + userId,
                orderService.getOrderByUserIdOrTrackingNumberOrCouponCode(userId, null, null)),
                HttpStatus.OK);
    }

    @GetMapping(path = "/order/trackingNumber/{trackingNumber}")
    public ResponseEntity<ResponseWrapper> getOrderByTrackingNumber(@RequestParam String trackingNumber) {
        return new ResponseEntity<>(new ResponseWrapper(
                "SUCCESS",
                "Order Details fetched for trackingNumber: " + trackingNumber,
                orderService.getOrderByUserIdOrTrackingNumberOrCouponCode(null, trackingNumber, null)),
                HttpStatus.OK);
    }

    @GetMapping(path = "/order/couponCode/{couponCode}")
    public ResponseEntity<ResponseWrapper> getOrderByCouponCode(@RequestParam String couponCode) {
        return new ResponseEntity<>(new ResponseWrapper(
                "SUCCESS",
                "Order Details fetched for couponCode: " + couponCode,
                orderService.getOrderByUserIdOrTrackingNumberOrCouponCode(null, null, couponCode)),
                HttpStatus.OK);
    }

    @PostMapping(path = "/order")
    public ResponseEntity<ResponseWrapper> placeOrder(@RequestBody Order order) {
        return new ResponseEntity<>(new ResponseWrapper(
                "SUCCESS",
                "Order placed successfully",
                orderService.placeOrder(order)),
                HttpStatus.CREATED);
    }

    @PutMapping(path = "/order")
    public ResponseEntity<ResponseWrapper> updateOrder(@RequestBody Order order) {
        return new ResponseEntity<>(new ResponseWrapper(
                "SUCCESS",
                "Order updated successfully",
                orderService.updateOrder(order)),
                HttpStatus.CREATED);
    }

    // TODO: need to create new entity
    @PostMapping(path = "/order/calculate")
    public ResponseEntity<ResponseWrapper> calculateOrder(@RequestBody Object object) {
        return new ResponseEntity<>(new ResponseWrapper(
                "SUCCESS",
                "Order amount calculated",
                orderService.calculateOrder(object)),
                HttpStatus.OK);
    }

    // TODO:need to implement logic
    @PostMapping(path = "/order/prepare")
    public boolean prepareOrder(@RequestBody Order order) {
        return orderService.prepareOrder(order);
    }
}
