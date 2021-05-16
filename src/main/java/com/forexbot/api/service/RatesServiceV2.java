package com.forexbot.api.service;

import com.forexbot.api.dao.rates.ForexData;
import com.forexbot.api.dao.rates.ForexRates;

import java.util.List;

public interface RatesServiceV2 {

	void updateRates(List<ForexData> ratesRequest, String approvedBy);

	void goLive();
	
	List<ForexRates> getExchangeRates();
}