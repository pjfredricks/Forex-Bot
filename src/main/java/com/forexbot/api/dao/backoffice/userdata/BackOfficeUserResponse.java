package com.forexbot.api.dao.backoffice.userdata;

import com.forexbot.api.dao.backoffice.UserCategory;
import com.forexbot.api.dao.backoffice.address.Address;
import com.forexbot.api.dao.backoffice.vendor.VendorData;
import lombok.Data;

@Data
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
}
