package com.forexbot.api.service.impl;

import com.forexbot.api.dao.admin.*;
import com.forexbot.api.repository.AdminRepository;
import com.forexbot.api.repository.VendorRepository;
import com.forexbot.api.service.BackOfficeService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class BackOfficeServiceImpl implements BackOfficeService {

    private final AdminRepository adminRepository;
    private final VendorRepository vendorRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public BackOfficeServiceImpl(AdminRepository adminRepository, VendorRepository vendorRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.adminRepository = adminRepository;
        this.vendorRepository = vendorRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public AdminData loginAdmin(AdminRequest adminRequest) throws Exception {
        AdminData adminData = adminRepository.getAdminDataByUserName(adminRequest.getUserName());
        if (ObjectUtils.isNotEmpty(adminData) && checkPasswordsMatch(adminRequest.getPassword(), adminData.getPassword())) {
            return adminData;
        }
        throw new Exception("Login failed");
    }

    @Override
    public VendorResponse loginVendor(VendorRequest vendorRequest) throws Exception {
        VendorData vendorData = vendorRepository.getVendorDataByUserName(vendorRequest.getUserName());
        if (ObjectUtils.isNotEmpty(vendorData) && checkPasswordsMatch(vendorRequest.getPassword(), vendorData.getPassword())) {
            return new VendorResponse(vendorData.getUserName(), vendorData.getAgentId());
        }
        throw new Exception("Login failed");
    }

    @Override
    @Transactional
    public void createAdmin(AdminRequest adminRequest) {
        AdminData adminData = mapAdminRequestToData(adminRequest);
        adminRepository.save(adminData);
    }

    @Override
    @Transactional
    public void createVendor(VendorRequest vendorRequest) {
        VendorData vendorData = mapVendorRequestToData(vendorRequest);
        vendorRepository.save(vendorData);
    }

    private boolean checkPasswordsMatch(String enteredPassword, String passwordFromDb) {
        return bCryptPasswordEncoder.matches(enteredPassword, passwordFromDb);
    }

    private AdminData mapAdminRequestToData(AdminRequest request) {
        AdminData adminData = new AdminData();
        BeanUtils.copyProperties(request, adminData);
        adminData.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        return adminData;
    }

    private VendorData mapVendorRequestToData(VendorRequest request) {
        VendorData vendorData = new VendorData();
        BeanUtils.copyProperties(request, vendorData);
        vendorData.setPassword(bCryptPasswordEncoder.encode(request.getPassword()));
        vendorData.setAgentId(RandomStringUtils.randomAlphanumeric(12));
        return vendorData;
    }
}
