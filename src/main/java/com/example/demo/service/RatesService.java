package com.example.demo.service;

import com.example.demo.repository.dao.ForexRates;
import java.util.List;

public interface RatesService {

	void updateExchangeRates();
	
	List<ForexRates> getExchangeRates();
}