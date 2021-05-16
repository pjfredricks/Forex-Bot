package com.forexbot.api.dao.backoffice.userdata;

import com.forexbot.api.dao.backoffice.UserCategory;
import com.forexbot.api.dao.backoffice.address.Address;
import com.forexbot.api.dao.backoffice.vendor.VendorData;
import lombok.Data;

@Data
public class BackOfficeLoginResponse {
    private String userId;
    private String userName;
    private String mobileNum;
    private String emailId;
    private UserCategory userCategory;
    private VendorData vendorData;
    private Address address;
}
