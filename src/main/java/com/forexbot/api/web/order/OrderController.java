package com.forexbot.api.web.order;

import com.forexbot.api.dao.order.CalculateRequest;
import com.forexbot.api.dao.order.Order;
import com.forexbot.api.dao.order.OrderResponse;
import com.forexbot.api.dao.order.OrderType;
import com.forexbot.api.service.OrderService;
import com.forexbot.api.web.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Currency;

import static com.forexbot.api.web.utils.ResponseWrapper.*;

@Slf4j
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class OrderController {

    private final OrderService orderService;

    @GetMapping(path = "/order/customerId/{customerId}")
    public ResponseEntity<ResponseWrapper> getOrderByCustomerId(@PathVariable String customerId) {
        return ResponseEntity.ok(
                buildSuccessResponse("Order Details fetched for customerId: " + customerId,
                        orderService.getOrderByCustomerIdOrTrackingIdOrCouponCode(customerId, null, null)
                ));
    }

    @GetMapping(path = "/order/trackingId/{trackingId}")
    public ResponseEntity<ResponseWrapper> getOrderByTrackingId(@PathVariable String trackingId) {
        return ResponseEntity.ok(
                buildSuccessResponse("Order Details fetched for trackingId: " + trackingId,
                        orderService.getOrderByCustomerIdOrTrackingIdOrCouponCode(null, trackingId, null)
                ));
    }

    @GetMapping(path = "/order/couponCode/{couponCode}")
    public ResponseEntity<ResponseWrapper> getOrderByCouponCode(@PathVariable String couponCode) {
        return ResponseEntity.ok(
                buildSuccessResponse("Order Details fetched for couponCode: " + couponCode,
                        orderService.getOrderByCustomerIdOrTrackingIdOrCouponCode(null, null, couponCode)
                ));
    }

    @PostMapping(path = "/order")
    public ResponseEntity<ResponseWrapper> placeOrder(@RequestBody CalculateRequest request) {
        OrderResponse response;
        try {
            validateRequest(request);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.badRequest()
                    .body(buildSuccessResponse("Order validation failed", e.getMessage()));
        }

        response = orderService.placeOrder(request);
        if (response.getTransactionId() != null) {
            return new ResponseEntity<>(
                    buildSuccessResponse("Order placed successfully", response),
                    HttpStatus.CREATED);
        }
        return new ResponseEntity<>(
                buildSuccessResponse("Order placement failed", response),
                HttpStatus.CREATED);

    }

    @PutMapping(path = "/order")
    public ResponseEntity<ResponseWrapper> updateOrder(@RequestBody Order order) {
        return new ResponseEntity<>(
                buildSuccessResponse("Order updated successfully",
                        orderService.updateOrder(order)),
                HttpStatus.CREATED);
    }

    @PostMapping(path = "/order/calculateAmount")
    public ResponseEntity<ResponseWrapper> calculateOrder(@RequestBody CalculateRequest request) {
        try {
            validateRequest(request);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(buildErrorResponse("Order validation failed", e.getCause().getMessage()));
        }
        return ResponseEntity.ok(
                buildSuccessResponse("Order amount calculated", orderService.calculateOrder(request)
                ));
    }

    public static void validateRequest(CalculateRequest request) {
        try {
            Currency.getInstance(request.getCountryCode());
            if (request.getForexAmount() < 0) {
                throw new IllegalArgumentException("Amount not valid");
            }
            if (null == OrderType.valueOf(request.getOrderType())) {
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
