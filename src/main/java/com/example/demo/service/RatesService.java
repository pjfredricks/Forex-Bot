package com.example.demo.service;

import com.example.demo.repository.dao.rates.ForexRates;
import com.example.demo.repository.dao.order.OrderType;
import com.example.demo.repository.dao.rates.RatesRequest;

import java.util.List;

public interface RatesService {

	void updateExchangeRates();

	void updateExchangeRates(List<RatesRequest> ratesRequest);
	
	List<ForexRates> getExchangeRates();

	double getRateByCountryCodeAndType(String countryCode, OrderType orderType);
}