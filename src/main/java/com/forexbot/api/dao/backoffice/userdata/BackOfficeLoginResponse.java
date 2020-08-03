package com.forexbot.api.dao.backoffice.userdata;

import com.forexbot.api.dao.backoffice.address.Address;
import com.forexbot.api.dao.backoffice.UserCategory;
import com.forexbot.api.dao.backoffice.vendor.VendorData;

public class BackOfficeLoginResponse {
    private String userId;
    private String userName;
    private String mobileNum;
    private String emailId;
    private UserCategory userCategory;
    private VendorData vendorData;
    private Address address;

    public BackOfficeLoginResponse() {
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

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public UserCategory getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(UserCategory userCategory) {
        this.userCategory = userCategory;
    }

    public VendorData getVendorData() {
        return vendorData;
    }

    public void setVendorData(VendorData vendorData) {
        this.vendorData = vendorData;
    }

    public Address getAddress() {
        return address;
    }

    public void setAddress(Address address) {
        this.address = address;
    }
}
