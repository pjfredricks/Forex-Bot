package com.forexbot.api.service;

import com.forexbot.api.dao.admin.AdminData;
import com.forexbot.api.dao.admin.AdminRequest;
import com.forexbot.api.dao.admin.VendorRequest;
import com.forexbot.api.dao.admin.VendorResponse;

public interface BackOfficeService {

    AdminData loginAdmin(AdminRequest adminRequest) throws Exception;

    VendorResponse loginVendor(VendorRequest vendorRequest) throws Exception;

    void createAdmin(AdminRequest adminRequest);

    void createVendor(VendorRequest vendorRequest);
}
