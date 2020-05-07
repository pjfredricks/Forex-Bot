package com.example.demo.service;

import com.example.demo.repository.dao.userdata.UserData;
import com.example.demo.repository.dao.userdata.UserDataRequest;
import com.example.demo.repository.dao.userdata.UserDataResponse;

import java.util.UUID;

public interface UserDataService {

	UserDataResponse signUpUser(UserDataRequest userDataRequest) throws Exception;

	UserData getUserDataByEmailId(String emailId);

	UserData getUserDataByMobileNum(String mobileNum);

	UserDataResponse login(UserDataRequest userDataRequest);

	UserData getUserDetailsById(UUID userId);

	UserDataResponse resetPassword(UserDataRequest resetRequest) throws IllegalAccessException;

	UserDataResponse updateUserDetails(UserDataRequest resetRequest) throws IllegalAccessException;

	// TODO: Use mobileNum after sms impl
	String generateAndSaveOtp(String emailId, int otpType);

	boolean verifyOtp(String otp, String emailId, int otpType) throws IllegalAccessException;
}