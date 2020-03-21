package com.example.demo.repository.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "userDetail")
@Entity
public class UserData {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private int id;

	@NotNull
	@Column(name = "name")
	private String name;

	@NotNull
	@Column(name = "emailId")
	private String emailId;

	@NotNull
	@Column(name = "password")
	private String password;

	@NotNull
	@Column(name = "mobileNum")
	private String mobileNum;

	@NotNull
	@ApiModelProperty(hidden = true)
	@Column(name = "createDate")
	private String create_date;

	@ApiModelProperty(hidden = true)
	@Column(name = "modifiedDate")
	private String modified_date;

	public UserData() {
		super();
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

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getCreate_date() {
		return create_date;
	}

	public void setCreate_date(String create_date) {
		this.create_date = create_date;
	}

	public String getModified_date() {
		return modified_date;
	}

	public void setModified_date(String modified_date) {
		this.modified_date = modified_date;
	}

	@Override
	public String toString() {
		return "UserDetail{" +
				"id=" + id +
				", emailId='" + emailId + '\'' +
				", password='" + password + '\'' +
				", create_date='" + create_date + '\'' +
				", modified_date='" + modified_date + '\'' +
				'}';
	}
}
