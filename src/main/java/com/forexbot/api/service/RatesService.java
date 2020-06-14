package com.forexbot.api.service;

import com.forexbot.api.dao.order.OrderType;
import com.forexbot.api.dao.rates.ForexRates;
import com.forexbot.api.dao.rates.RatesRequest;

import java.util.List;

public interface RatesService {

	void updateExchangeRates();

	void updateExchangeRates(List<RatesRequest> ratesRequest);
	
	List<ForexRates> getExchangeRates();

	double getRateByCountryCodeAndType(String countryCode, OrderType orderType);
}