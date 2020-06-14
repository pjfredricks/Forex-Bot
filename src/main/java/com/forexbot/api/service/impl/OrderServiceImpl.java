package com.forexbot.api.service.impl;

import com.forexbot.api.dao.order.*;
import com.forexbot.api.dao.userdata.UserData;
import com.forexbot.api.repository.OrderRepository;
import com.forexbot.api.repository.UserDataRepository;
import com.forexbot.api.service.OrderService;
import com.forexbot.api.service.RatesService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static com.forexbot.api.web.utils.Constants.ZONE;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private RatesService ratesService;
    private UserDataRepository userDataRepository;

    public OrderServiceImpl(OrderRepository repository, RatesService ratesService, UserDataRepository userDataRepository) {
        this.orderRepository = repository;
        this.ratesService = ratesService;
        this.userDataRepository = userDataRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderByUserIdOrTrackingNumberOrCouponCode(String userId,
                                                              String trackingNumber,
                                                              String couponCode) {
        return orderRepository.getOrderByUserIdOrTrackingNumberOrCouponCode(userId, trackingNumber, couponCode);
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(CalculateRequest request) {
        OrderResponse orderResponse = new OrderResponse();
        UserData userData = userDataRepository.getUserDataByUserId(UUID.fromString(request.getUserId()));

        if (ObjectUtils.isEmpty(userData)) {
            orderResponse.setTransactionId(null);
            orderResponse.setUserExists(false);
            orderResponse.setEmailVerified(false);
            return orderResponse;
        }
        orderResponse.setUserExists(true);
        CalculateResponse response = calculateOrder(request);

        if (!userData.isEmailVerified()) {
            orderResponse.setTransactionId(null);
            orderResponse.setUserExists(true);
            orderResponse.setEmailVerified(false);
            return orderResponse;
        }
        orderResponse.setEmailVerified(true);

        Order order = createOrder(request, response);
        order.setStatus(OrderStatus.COMPLETED);

        orderResponse.setTransactionId(orderRepository.save(order).getTrackingNumber());
        return orderResponse;
    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public CalculateResponse calculateOrder(CalculateRequest request) {
        double rate = ratesService.getRateByCountryCodeAndType(request.getCountryCode(), OrderType.valueOf(request.getOrderType()));
        double discountAmount = 0.00d;
        double forexTotal = calculateForexTotal(rate, request.getForexAmount());
        double gstAmount = calculateGstAmount(forexTotal);
        int salesTotal = (int) (forexTotal + gstAmount - discountAmount);

        CalculateResponse response = new CalculateResponse();
        response.setUserId(request.getUserId());
        response.setDiscountAmount(discountAmount);
        response.setGst(gstAmount);
        response.setForexTotal(forexTotal);
        response.setSalesTotal(salesTotal);

        return response;
    }

    private Order createOrder(CalculateRequest request, CalculateResponse response) {
        Order order = new Order();
        order.setUserId(UUID.fromString(request.getUserId()));
        order.setCountryCode(request.getCountryCode());
        order.setTrackingNumber(RandomStringUtils.randomAlphanumeric(12));
        order.setCouponCode(request.getCouponCode());
        order.setCreateDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
        order.setOrderType(OrderType.valueOf(request.getOrderType()));
        order.setForexAmount(request.getForexAmount());
        order.setForexTotal(response.getForexTotal());
        order.setGst(response.getGst());
        order.setDiscountAmount(response.getDiscountAmount());
        order.setSalesTotal(response.getSalesTotal());
        return order;
    }

    private double calculateForexTotal(double rate, int forexAmount) {
        return BigDecimal.valueOf(rate * forexAmount).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    private double calculateGstAmount(double forexTotal) {
        return BigDecimal.valueOf((forexTotal * 0.18)).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }
}