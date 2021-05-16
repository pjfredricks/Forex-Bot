package com.forexbot.api.dao.backoffice.userdata;

import com.forexbot.api.dao.backoffice.address.AddressRequest;
import com.forexbot.api.dao.backoffice.vendor.VendorRequest;
import lombok.Data;

@Data
public class BackOfficeSignInRequest {
    private String createdBy;
    private String userName;
    private String password;
    private String emailId;
    private String mobileNum;
    private AddressRequest address;
    private VendorRequest vendorRequest;
}
