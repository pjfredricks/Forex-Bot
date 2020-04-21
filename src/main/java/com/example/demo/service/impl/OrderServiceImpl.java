package com.example.demo.service.impl;

import com.example.demo.repository.dao.Order.*;
import com.example.demo.repository.OrderRepository;
import com.example.demo.service.OrderService;
import com.example.demo.service.RatesService;
import com.example.demo.service.UserDataService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private RatesService ratesService;
    private UserDataService userDataService;

    public OrderServiceImpl(OrderRepository repository, RatesService ratesService, UserDataService userDataService) {
        this.orderRepository = repository;
        this.ratesService = ratesService;
        this.userDataService = userDataService;
    }

    @Override
    public Order getOrderByUserIdOrTrackingNumberOrCouponCode(String userId,
                                                              String trackingNumber,
                                                              String couponCode) {
        return orderRepository.getOrderByUserIdOrTrackingNumberOrCouponCode(userId, trackingNumber, couponCode);
    }

    @Override
    @Transactional
    public String placeOrder(CalculateRequest request) {
        if (ObjectUtils.isEmpty(userDataService.getUserDetailsById(UUID.fromString(request.getUserId())))) {
            return "";
        }
        CalculateResponse response = calculateOrder(request);

        Order order = new Order();
        order.setUserId(UUID.fromString(request.getUserId()));
        order.setCountryCode(request.getCountryCode());
        order.setTrackingNumber(RandomStringUtils.randomAlphanumeric(12));
        order.setCouponCode(request.getCouponCode());
        order.setCreateDate(LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toString());
        order.setOrderType(OrderType.valueOf(request.getOrderType()));
        order.setForexAmount(request.getForexAmount());
        order.setForexTotal(response.getForexTotal());
        order.setGst(response.getGst());
        order.setDiscountAmount(response.getDiscountAmount());
        order.setSalesTotal(response.getSalesTotal());

        // TODO : update status based on email and sms
        order.setStatus(OrderStatus.COMPLETED);

        return orderRepository.save(order).getTrackingNumber();
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

    private double calculateForexTotal(double rate, int forexAmount) {
        return rate * forexAmount;
    }

    private double calculateGstAmount(double forexTotal) {
        return BigDecimal.valueOf((forexTotal / 100 * 18)).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }
}
