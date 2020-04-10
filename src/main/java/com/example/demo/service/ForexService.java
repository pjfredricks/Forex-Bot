package com.example.demo.service;

import com.example.demo.repository.dao.ForexRates;
import com.example.demo.repository.dao.UserDataRequest;
import com.example.demo.repository.dao.UserDataResponse;

import java.util.List;

public interface ForexService {

	List<ForexRates> updateExchangeRates();
	
	List<ForexRates> getExchangeRates();

	UserDataResponse signUpUser(UserDataRequest userDataRequest) throws Exception;

	UserDataResponse login(UserDataRequest userDataRequest) throws Exception;

	// TODO : need more methods for calculating amount, sending sms and email, and updating vendor details
}