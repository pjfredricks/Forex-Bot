package com.example.demo.web;

import com.example.demo.repository.dao.ResponseWrapper;
import com.example.demo.repository.dao.order.CalculateRequest;
import com.example.demo.repository.dao.order.Order;
import com.example.demo.repository.dao.order.OrderType;
import com.example.demo.service.OrderService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;
import java.util.UUID;

import static com.example.demo.web.utils.Constants.ERROR;
import static com.example.demo.web.utils.Constants.SUCCESS;

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
                SUCCESS,
                "Order Details fetched for userId: " + userId,
                orderService.getOrderByUserIdOrTrackingNumberOrCouponCode(userId, null, null)),
                HttpStatus.OK);
    }

    @GetMapping(path = "/order/trackingNumber/{trackingNumber}")
    public ResponseEntity<ResponseWrapper> getOrderByTrackingNumber(@RequestParam String trackingNumber) {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Order Details fetched for trackingNumber: " + trackingNumber,
                orderService.getOrderByUserIdOrTrackingNumberOrCouponCode(null, trackingNumber, null)),
                HttpStatus.OK);
    }

    @GetMapping(path = "/order/couponCode/{couponCode}")
    public ResponseEntity<ResponseWrapper> getOrderByCouponCode(@RequestParam String couponCode) {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Order Details fetched for couponCode: " + couponCode,
                orderService.getOrderByUserIdOrTrackingNumberOrCouponCode(null, null, couponCode)),
                HttpStatus.OK);
    }

    @PostMapping(path = "/order")
    public ResponseEntity<ResponseWrapper> placeOrder(@RequestBody CalculateRequest request) {
        try {
            validateRequest(request);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Order validation failed",
                    e.getCause().getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        String trackingNumber = orderService.placeOrder(request);

        if (StringUtils.isNotBlank(trackingNumber)) {
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Order placed successfully",
                    trackingNumber),
                    HttpStatus.CREATED);
        }

        return new ResponseEntity<>(new ResponseWrapper(
                ERROR,
                "User validation failed",
                "User does not exist"),
                HttpStatus.BAD_REQUEST);
    }

    @PutMapping(path = "/order")
    public ResponseEntity<ResponseWrapper> updateOrder(@RequestBody Order order) {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Order updated successfully",
                orderService.updateOrder(order)),
                HttpStatus.CREATED);
    }

    @PostMapping(path = "/order/calculateAmount")
    public ResponseEntity<ResponseWrapper> calculateOrder(@RequestBody CalculateRequest request) {
        try {
            validateRequest(request);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Order validation failed",
                    e.getCause().getMessage()),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Order amount calculated",
                orderService.calculateOrder(request)),
                HttpStatus.OK);
    }

    public static void validateRequest(CalculateRequest request) {
        try {
            UUID.fromString(request.getUserId());
            Currency.getInstance(request.getCountryCode());
            if (request.getForexAmount() < 0) {
                throw new IllegalArgumentException("Amount not valid");
            }
            if (!StringUtils.isNotBlank(OrderType.valueOf(request.getOrderType()).toString())) {
                throw new IllegalArgumentException("OrderType not valid");
            }
            if (null == request.getCouponCode()) {
                throw new IllegalArgumentException("null coupon code");
            }

        } catch (Exception e) {
            throw new IllegalArgumentException(e);
        }
    }
}
