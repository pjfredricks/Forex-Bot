package com.example.demo.service;

import com.example.demo.repository.dao.userdata.ResetPasswordRequest;
import com.example.demo.repository.dao.userdata.UserData;
import com.example.demo.repository.dao.userdata.UserDataRequest;
import com.example.demo.repository.dao.userdata.UserDataResponse;

import java.util.UUID;

public interface UserDataService {

	UserDataResponse signUpUser(UserDataRequest userDataRequest) throws Exception;

	UserData getUserDataByEmailIdOrMobileNum(String emailId, String mobileNum);

	UserDataResponse login(UserDataRequest userDataRequest) throws Exception;

	UserData getUserDetailsById(UUID userId);

	UserDataResponse resetPassword(ResetPasswordRequest resetRequest) throws IllegalAccessException;
}