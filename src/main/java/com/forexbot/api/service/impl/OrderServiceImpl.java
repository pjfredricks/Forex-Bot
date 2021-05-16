package com.forexbot.api.service.impl;

import com.forexbot.api.dao.order.*;
import com.forexbot.api.dao.customer.CustomerData;
import com.forexbot.api.repository.OrderRepository;
import com.forexbot.api.repository.CustomerRepository;
import com.forexbot.api.service.OrderService;
import com.forexbot.api.service.RatesService;
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
    private final CustomerRepository customerRepository;

    public OrderServiceImpl(OrderRepository repository, RatesService ratesService, CustomerRepository customerRepository) {
        this.orderRepository = repository;
        this.ratesService = ratesService;
        this.customerRepository = customerRepository;
    }

    @Override
    public List<Order> getAllOrders() {
        return orderRepository.findAll();
    }

    @Override
    public Order getOrderByCustomerIdOrTrackingIdOrCouponCode(String customerId,
                                                              String trackingId,
                                                              String couponCode) {
        return orderRepository.getOrderByCustomerIdOrTrackingIdOrCouponCode(UUID.fromString(customerId), trackingId, couponCode);
    }

    @Override
    @Transactional
    public OrderResponse placeOrder(CalculateRequest request) {
        CustomerData customerData = customerRepository.getCustomerDataByCustomerId(UUID.fromString(request.getCustomerId()));

        OrderResponse orderResponse = new OrderResponse();
        if (null == customerData) {
            return orderResponse;
        }
        orderResponse.setCustomerExists(true);

        if (!customerData.isEmailVerified()) {
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
        response.setCustomerId(request.getCustomerId());
        response.setDiscountAmount(discountAmount);
        response.setGst(gstAmount);
        response.setForexTotal(forexTotal);
        response.setSalesTotal(salesTotal);

        return response;
    }

    private Order createOrder(CalculateRequest request, CalculateResponse response) {
        Order order = new Order();
        BeanUtils.copyProperties(request, order, "customerId");
        BeanUtils.copyProperties(response, order, "customerId");

        order.setCustomerId(UUID.fromString(request.getCustomerId()));
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
