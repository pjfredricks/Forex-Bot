package com.example.demo.service;

import com.example.demo.repository.dao.userdata.UserData;
import com.example.demo.repository.dao.userdata.UserDataRequest;
import com.example.demo.repository.dao.userdata.UserDataResponse;

import java.util.UUID;

public interface UserDataService {

	UserDataResponse signUpUser(UserDataRequest userDataRequest) throws Exception;

	UserDataResponse login(UserDataRequest userDataRequest) throws Exception;

	UserData getUserDetailsById(UUID userId);

	// TODO : need more methods for calculating amount, sending sms and email, and updating vendor details
}