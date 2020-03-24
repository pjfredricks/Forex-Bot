package com.example.demo.repository.dao;

public class UserDataResponse {

	private int id;
	private String name;
	private String emailId;
	private String mobileNum;

	public UserDataResponse() {
		super();
	}

	public static UserDataResponse map(UserDataRequest userDataRequest) {
		UserDataResponse response = new UserDataResponse();
		response.setId(userDataRequest.getId());
		response.setEmailId(userDataRequest.getEmailId());
		response.setMobileNum(userDataRequest.getMobileNum());
		response.setName(userDataRequest.getName());
		return response;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
	}

	public String getMobileNum() {
		return mobileNum;
	}

	public void setMobileNum(String mobileNum) {
		this.mobileNum = mobileNum;
	}
}
