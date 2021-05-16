package com.forexbot.api.service;

import com.forexbot.api.dao.order.OrderType;
import com.forexbot.api.dao.rates.ForexRates;
import com.forexbot.api.dao.rates.ForexData;

import java.util.List;

public interface RatesService {

	void updateRates(String approvedBy);

	void updateRates(List<ForexData> ratesRequest, String approvedBy);
	
	List<ForexRates> getExchangeRates();

	double getRateByCountryCodeAndType(String countryCode, OrderType orderType);

	void deleteTempRates();
}