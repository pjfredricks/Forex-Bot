package com.forexbot.api.dao.backoffice.userdata;

import com.forexbot.api.dao.backoffice.address.AddressRequest;
import com.forexbot.api.dao.backoffice.vendor.VendorRequest;

public class BackOfficeSignInRequest {
    private String createdBy;
    private String userName;
    private String password;
    private String emailId;
    private String mobileNum;
    private AddressRequest address;
    private VendorRequest vendorRequest;

    public BackOfficeSignInRequest() {
        super();
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
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

    public AddressRequest getAddress() {
        return address;
    }

    public void setAddress(AddressRequest address) {
        this.address = address;
    }

    public VendorRequest getVendorRequest() {
        return vendorRequest;
    }

    public void setVendorRequest(VendorRequest vendorRequest) {
        this.vendorRequest = vendorRequest;
    }
}
