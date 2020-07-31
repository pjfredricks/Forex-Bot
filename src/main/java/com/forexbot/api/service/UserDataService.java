package com.forexbot.api.service;

import com.forexbot.api.dao.otp.OtpRequest;
import com.forexbot.api.dao.userdata.UserData;
import com.forexbot.api.dao.userdata.UserDataRequest;
import com.forexbot.api.dao.userdata.UserDataResponse;

import java.util.List;
import java.util.UUID;

public interface UserDataService {

	List<UserData> getAllUsers();

	UserDataResponse signUpUser(UserDataRequest userDataRequest) throws Exception;

	UserData getUserDataByEmailIdOrMobileNum(String emailId, String mobileNum);

	UserDataResponse login(UserDataRequest userDataRequest);

	UserData getUserDetailsById(UUID userId);

	UserDataResponse resetPassword(UserDataRequest resetRequest) throws IllegalAccessException;

	UserDataResponse updateUserDetails(UserDataRequest resetRequest) throws IllegalAccessException;

	String generateAndSaveOtp(OtpRequest otpRequest) throws IllegalAccessException;

	boolean verifyOtp(OtpRequest otpRequest) throws IllegalAccessException;

	void verifyEmail(UserDataRequest userDataRequest) throws IllegalAccessException;
}