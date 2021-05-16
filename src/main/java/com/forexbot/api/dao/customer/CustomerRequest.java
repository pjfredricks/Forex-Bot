package com.forexbot.api.dao.customer;

import lombok.Data;

@Data
public class CustomerRequest {
	private String customerId;
	private String name;
	private String emailId;
	private String password;
	private String mobileNum;
	private String modifiedBy;
}
