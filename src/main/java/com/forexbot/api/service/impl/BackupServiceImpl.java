package com.forexbot.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forexbot.api.dao.order.Order;
import com.forexbot.api.dao.otp.OtpData;
import com.forexbot.api.dao.userdata.UserData;
import com.forexbot.api.repository.OrderRepository;
import com.forexbot.api.repository.OtpDataRepository;
import com.forexbot.api.repository.UserDataRepository;
import com.forexbot.api.service.BackupService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackupServiceImpl implements BackupService {

    private OrderRepository orderRepository;
    private UserDataRepository userDataRepository;
    private OtpDataRepository otpDataRepository;
    private ObjectMapper mapper;

    public BackupServiceImpl(OrderRepository orderRepository,
                             UserDataRepository userDataRepository,
                             OtpDataRepository otpDataRepository,
                             ObjectMapper mapper) {
        this.orderRepository = orderRepository;
        this.userDataRepository = userDataRepository;
        this.otpDataRepository = otpDataRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean runBackup() {
        List<Order> orders = orderRepository.findAll();
        List<UserData> users = userDataRepository.findAll();
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
