package com.example.demo.service.impl;

import com.example.demo.repository.OrderRepository;
import com.example.demo.repository.UserDataRepository;
import com.example.demo.repository.dao.order.Order;
import com.example.demo.repository.dao.userdata.UserData;
import com.example.demo.service.BackupService;
import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class BackupServiceImpl implements BackupService {

    private OrderRepository orderRepository;
    private UserDataRepository userDataRepository;
    private ObjectMapper mapper;

    public BackupServiceImpl(OrderRepository orderRepository, UserDataRepository userDataRepository, ObjectMapper mapper) {
        this.orderRepository = orderRepository;
        this.userDataRepository = userDataRepository;
        this.mapper = mapper;
    }

    @Override
    public boolean runBackup() {
        List<Order> orders = orderRepository.findAll();
        List<UserData> users = userDataRepository.findAll();
        try {
            mapper.writeValueAsString(orders);
            mapper.writeValueAsString(users);
        } catch (JsonProcessingException e) {
            e.printStackTrace();
        }
        return false;
    }
}
