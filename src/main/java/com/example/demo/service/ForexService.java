package com.example.demo.service;

import com.example.demo.repository.dao.ForexModel;
import com.example.demo.repository.dao.UserData;

import java.util.List;

public interface ForexService {

	void updateExchangeRates();
	
	List<ForexModel> getExchangeRates();

	String signUpUser(UserData userData) throws Exception;

	Boolean login(String userName, String emailId, String password) throws Exception;

	// TODO : need more methods for calculating amount, sending sms and email, and updating vendor details
}