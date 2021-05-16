package com.forexbot.api.dao.customer;

import lombok.Data;

@Data
public class CustomerResponse {
	private String customerId;
	private String name;
	private String emailId;
	private String mobileNum;
}
