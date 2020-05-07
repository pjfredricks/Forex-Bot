package com.example.demo.service;

import com.example.demo.repository.dao.otp.OtpRequest;
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

	String generateAndSaveOtp(OtpRequest otpRequest);

	boolean verifyOtp(OtpRequest otpRequest) throws IllegalAccessException;

	void verifyEmail(UserDataRequest userDataRequest) throws IllegalAccessException;
}