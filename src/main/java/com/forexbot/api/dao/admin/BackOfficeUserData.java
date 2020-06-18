package com.forexbot.api.dao.admin;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "backOfficeUserData")
@Entity
public class BackOfficeUserData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private int id;

    @NotNull
    @Column(name = "userId", unique = true)
    private String userId;

    @NotNull
    @Column(name = "userName", unique = true)
    private String userName;

    @NotNull
    @Column(name = "userCategory")
    private UserCategory userCategory;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "emailId", unique = true)
    private String emailId;

    @NotNull
    @Column(name = "mobileNum", unique = true)
    private String mobileNum;

    @NotNull
    @Column(name = "addressId", unique = true)
    private String addressId;

    @Column(name = "vendorId", unique = true)
    private String vendorId;

    public BackOfficeUserData() {
        super();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public UserCategory getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(UserCategory userCategory) {
        this.userCategory = userCategory;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
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

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getVendorAgentId() {
        return vendorId;
    }

    public void setVendorAgentId(String vendorAgentId) {
        this.vendorId = vendorAgentId;
    }
}
