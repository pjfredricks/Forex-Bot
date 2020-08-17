package com.forexbot.api.service;

import com.forexbot.api.dao.customer.CustomerDataResponse;
import com.forexbot.api.dao.otp.OtpRequest;
import com.forexbot.api.dao.customer.CustomerData;
import com.forexbot.api.dao.customer.CustomerRequest;
import com.forexbot.api.dao.customer.CustomerResponse;

import java.util.List;
import java.util.UUID;

public interface CustomerService {

	List<CustomerDataResponse> getAllCustomers();

	CustomerResponse signUpCustomer(CustomerRequest customerRequest) throws Exception;

	CustomerData getCustomerDataByEmailIdOrMobileNum(String emailId, String mobileNum);

	CustomerResponse login(CustomerRequest customerRequest) throws IllegalAccessException;

	CustomerDataResponse getCustomerIdDetailsById(UUID customerId);

	CustomerResponse resetCustomerPassword(CustomerRequest resetRequest) throws IllegalAccessException;

	CustomerResponse updateCustomerDetails(CustomerRequest resetRequest) throws IllegalAccessException;

	String generateAndSaveOtp(OtpRequest otpRequest) throws IllegalAccessException;

	boolean verifyOtp(OtpRequest otpRequest) throws IllegalAccessException;

	void verifyEmail(CustomerRequest customerRequest) throws IllegalAccessException;

	void deleteCustomer(UUID customerId, String deletedBy);
}