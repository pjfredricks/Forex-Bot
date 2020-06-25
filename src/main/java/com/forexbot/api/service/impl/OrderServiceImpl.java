package com.forexbot.api.service.impl;

import com.forexbot.api.dao.order.*;
import com.forexbot.api.dao.userdata.UserData;
import com.forexbot.api.repository.OrderRepository;
import com.forexbot.api.repository.UserDataRepository;
import com.forexbot.api.service.OrderService;
import com.forexbot.api.service.RatesService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
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

    private final OrderRepository orderRepository;
    private final RatesService ratesService;
    private final UserDataRepository userDataRepository;

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
    public Order getOrderByUserIdOrTrackingIdOrCouponCode(String userId,
                                                              String trackingId,
                                                              String couponCode) {
        return orderRepository.getOrderByUserIdOrTrackingIdOrCouponCode(UUID.fromString(userId), trackingId, couponCode);
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(CalculateRequest request) {
        UserData userData = userDataRepository.getUserDataByUserId(UUID.fromString(request.getUserId()));

        OrderResponse orderResponse = new OrderResponse();
        if (ObjectUtils.isEmpty(userData)) {
            return orderResponse;
        }
        orderResponse.setUserExists(true);

        if (!userData.isEmailVerified()) {
            return orderResponse;
        }
        orderResponse.setEmailVerified(true);

        CalculateResponse response = calculateOrder(request);
        Order order = createOrder(request, response);
        order.setOrderRate(response.getForexTotal()/request.getForexAmount());

        //TODO: update status based on vendor
        order.setStatus(OrderStatus.COMPLETED);

        orderResponse.setTransactionId(orderRepository.save(order).getTrackingId());
        return orderResponse;
    }

    @Override
    @Transactional
    public Order updateOrder(Order order) {
        return orderRepository.save(order);
    }

    @Override
    public CalculateResponse calculateOrder(CalculateRequest request) {
        double rate = ratesService.getRateByCountryCodeAndType(request.getCountryCode(),
                OrderType.valueOf(request.getOrderType()));
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
        BeanUtils.copyProperties(request, order, "userId");
        BeanUtils.copyProperties(response, order, "userId");

        order.setUserId(UUID.fromString(request.getUserId()));
        order.setTrackingId(RandomStringUtils.randomAlphanumeric(12));
        order.setCreateDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
        order.setOrderType(OrderType.valueOf(request.getOrderType()));

        return order;
    }

    private double calculateForexTotal(double rate, int forexAmount) {
        return BigDecimal.valueOf(rate * forexAmount).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    private double calculateGstAmount(double forexTotal) {
        return BigDecimal.valueOf((forexTotal * 0.18)).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }
}
