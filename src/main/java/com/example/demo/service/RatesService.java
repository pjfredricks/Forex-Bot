package com.example.demo.service;

import com.example.demo.repository.dao.ForexRates;
import com.example.demo.repository.dao.Order.OrderType;

import java.util.List;

public interface RatesService {

	void updateExchangeRates();
	
	List<ForexRates> getExchangeRates();

	Double getRateByCountryCodeAndType(String countryCode, OrderType orderType);
}