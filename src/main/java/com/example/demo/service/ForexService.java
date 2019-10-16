package com.example.demo.service;

import java.util.List;

import com.example.demo.repository.dao.ForexModel;

public interface ForexService {

	public void updateExchangeRates();
	
	public List<ForexModel> getExchangeRates();

	// TODO : need more methods for calculating amount, login, sending sms, email, and updating vendor details
}