package com.example.demo.service;

import com.example.demo.repository.dao.ForexModel;
import com.example.demo.repository.dao.UserDetail;

import java.util.List;

public interface ForexService {

	void updateExchangeRates();
	
	List<ForexModel> getExchangeRates();

	Boolean signUpUser(UserDetail userDetail);

	Boolean login(String emailId, String password);

	Boolean checkUserExists(String emailId);

	// TODO : need more methods for calculating amount, sending sms and email, and updating vendor details
}