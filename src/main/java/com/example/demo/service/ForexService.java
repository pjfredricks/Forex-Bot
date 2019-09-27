package com.example.demo.service;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.stereotype.Service;

public interface ForexService {

	Map<String, BigDecimal> getExchangeRates();

	// TODO : need more methods for calculating amount, login, sending sms, email, and updating vendor details
}