package com.forexbot.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forexbot.api.dao.order.Order;
import com.forexbot.api.dao.otp.OtpData;
import com.forexbot.api.dao.customer.CustomerData;
import com.forexbot.api.repository.OrderRepository;
import com.forexbot.api.repository.OtpDataRepository;
import com.forexbot.api.repository.CustomerRepository;
import com.forexbot.api.service.BackupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackupServiceImpl implements BackupService {

    private OrderRepository orderRepository;
    private CustomerRepository customerRepository;
    private OtpDataRepository otpDataRepository;
    private ObjectMapper mapper;

    public BackupServiceImpl(OrderRepository orderRepository,
                             CustomerRepository customerRepository,
                             OtpDataRepository otpDataRepository,
                             ObjectMapper mapper) {
        this.orderRepository = orderRepository;
        this.customerRepository = customerRepository;
        this.otpDataRepository = otpDataRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean runBackup() {
        List<Order> orders = orderRepository.findAll();
        List<CustomerData> users = customerRepository.findAll();
        List<OtpData> otpDataList = otpDataRepository.findAll();
        try {
            mapper.writeValueAsString(orders);
            mapper.writeValueAsString(users);
            mapper.writeValueAsString(otpDataList);
            return true;
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
