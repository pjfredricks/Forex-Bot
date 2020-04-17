package com.example.demo.service.impl;

import com.example.demo.repository.dao.Order.CalculateResponse;
import com.example.demo.repository.dao.Order.Order;
import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.dao.Order.CalculateRequest;
import com.example.demo.service.OrderService;
import com.example.demo.service.RatesService;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.stereotype.Service;

@Service
public class OrderServiceImpl implements OrderService {

    private OrderRepository orderRepository;
    private RatesService ratesService;

    public OrderServiceImpl(OrderRepository repository, RatesService ratesService) {
        this.orderRepository = repository;
        this.ratesService = ratesService;
    }

    @Override
    public Order getOrderByUserIdOrTrackingNumberOrCouponCode(String userId,
                                                              String trackingNumber,
                                                              String couponCode) {
        return orderRepository.getOrderByUserIdOrTrackingNumberOrCouponCode(userId, trackingNumber, couponCode);
    }

    @Override
    public String placeOrder(Order order) {
        order.setTrackingNumber(RandomStringUtils.randomAlphanumeric(12));
        return orderRepository.save(order).getTrackingNumber();
    }

    @Override
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public CalculateResponse calculateOrder(CalculateRequest request) {
        double rate = ratesService.getRateByCountryCodeAndType(request.getCountryCode(), request.getType());
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

    private Double calculateForexTotal(Double rate, int forexAmount) {
        return rate * forexAmount;
    }

    private Double calculateGstAmount(double forexTotal) {
        return forexTotal / 100 * 18;
    }
}
