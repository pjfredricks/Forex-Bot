package com.example.demo.service;

import com.example.demo.repository.dao.UserData.UserData;
import com.example.demo.repository.dao.UserData.UserDataRequest;
import com.example.demo.repository.dao.UserData.UserDataResponse;

import java.util.UUID;

public interface UserDataService {

	UserDataResponse signUpUser(UserDataRequest userDataRequest) throws Exception;

	UserDataResponse login(UserDataRequest userDataRequest) throws Exception;

	UserData getUserDetailsById(UUID userId);

	// TODO : need more methods for calculating amount, sending sms and email, and updating vendor details
}