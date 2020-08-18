package com.forexbot.api.dao.backoffice.userdata;

import com.forexbot.api.dao.backoffice.UserCategory;
import com.forexbot.api.dao.backoffice.address.Address;
import com.forexbot.api.dao.backoffice.vendor.VendorData;

public class BackOfficeUserResponse {

    private String createdBy;
    private String userId;
    private String userName;
    private UserCategory userCategory;
    private String emailId;
    private String password;
    private String mobileNum;
    private Address address;
    private VendorData vendorData;

    public BackOfficeUserResponse() {
        super();
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }

    public VendorData getVendorData() {
        return vendorData;
    }

    public void setVendorData(VendorData vendorData) {
        this.vendorData = vendorData;
    }
}
