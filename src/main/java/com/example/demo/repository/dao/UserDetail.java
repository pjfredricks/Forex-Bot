package com.example.demo.repository.dao;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "userDetail")
@Entity
public class UserDetail {

	@Id
	@GeneratedValue(strategy = GenerationType.AUTO)
	@ApiModelProperty(hidden = true)
	@JsonIgnore
	private int id;

	@NotNull
	@Column(name = "emailId")
	private String emailId;

	@NotNull
	@Column(name = "password")
	private String password;

	@NotNull
	@ApiModelProperty(hidden = true)
	@Column(name = "createDate")
	private String create_date;

	@ApiModelProperty(hidden = true)
	@Column(name = "modifiedDate")
	private String modified_date;

	public UserDetail() {
		super();
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getEmailId() {
		return emailId;
	}

	public void setEmailId(String emailId) {
		this.emailId = emailId;
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
