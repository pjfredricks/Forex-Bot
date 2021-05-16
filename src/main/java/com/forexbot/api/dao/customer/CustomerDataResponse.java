package com.forexbot.api.dao.customer;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class CustomerDataResponse {
	private String customerId;
	private String name;
	private String password;
	private String emailId;
	private String mobileNum;
	private boolean isEmailVerified;
	private boolean isMobileVerified;
}
