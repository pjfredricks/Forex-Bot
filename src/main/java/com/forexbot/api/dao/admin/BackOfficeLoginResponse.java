package com.forexbot.api.dao.admin;

public class BackOfficeLoginResponse {
    private String userName;
    private VendorData vendorData;
    private Address address;

    public BackOfficeLoginResponse() {
    }

    public BackOfficeLoginResponse(String userName, VendorData vendorData, Address address) {
        this.userName = userName;
        this.vendorData = vendorData;
        this.address = address;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
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
