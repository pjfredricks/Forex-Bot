package com.forexbot.api.service.impl;

import com.forexbot.api.dao.admin.*;
import com.forexbot.api.repository.AddressRepository;
import com.forexbot.api.repository.BackOfficeRepository;
import com.forexbot.api.repository.VendorRepository;
import com.forexbot.api.service.BackOfficeService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
public class BackOfficeServiceImpl implements BackOfficeService {

    private final BackOfficeRepository backOfficeRepository;
    private final AddressRepository addressRepository;
    private final VendorRepository vendorRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public BackOfficeServiceImpl(BackOfficeRepository backOfficeRepository,
                                 AddressRepository addressRepository,
                                 VendorRepository vendorRepository,
                                 BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.backOfficeRepository = backOfficeRepository;
        this.addressRepository = addressRepository;
        this.vendorRepository = vendorRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    public BackOfficeLoginResponse login(BackOfficeLoginRequest loginRequest) throws Exception {
        BackOfficeUserData userData = backOfficeRepository.getBackOfficeUserDataByEmailId(loginRequest.getEmailId());

        String vendorAgentId = userData.getVendorAgentId() != null ? userData.getVendorAgentId() : "";
        VendorData vendorData = vendorRepository.getVendorDataByVendorAgentId(vendorAgentId);

        Address address = addressRepository.getAddressByAddressId(userData.getAddressId());

        if (checkPasswordsMatch(loginRequest.getPassword(), userData.getPassword())) {
            BackOfficeLoginResponse response = new BackOfficeLoginResponse();
            BeanUtils.copyProperties(userData, response);

            response.setAddress(address);
            response.setVendorData(vendorData);
            return response;
        }

        throw new Exception("Login failed");
    }

    @Override
    @Transactional
    public BackOfficeUserData createAdmin(BackOfficeSignInRequest signInRequest) {
        BackOfficeUserData userData = createAndSaveAdminData(signInRequest);
        return backOfficeRepository.save(userData);
    }

    @Override
    @Transactional
    public BackOfficeUserData createVendor(BackOfficeSignInRequest signInRequest) {
        BackOfficeUserData userData = createAndSaveVendorData(signInRequest);
        return backOfficeRepository.save(userData);
    }

    @Override
    public List<BackOfficeUserResponse> getAllUsers() {
        List<BackOfficeUserData> userDataList = backOfficeRepository.findAll();
        return userDataList
                .stream()
                .map(this::createUserResponseFromData)
                .collect(Collectors.toList());
    }

    private boolean checkPasswordsMatch(String enteredPassword, String passwordFromDb) {
        return bCryptPasswordEncoder.matches(enteredPassword, passwordFromDb);
    }

    private BackOfficeUserData createAndSaveAdminData(BackOfficeSignInRequest signInRequest) {
        BackOfficeUserData userData = createBackOfficeUserData(signInRequest);
        userData.setUserCategory(UserCategory.ADMIN);

        return userData;
    }

    private BackOfficeUserData createAndSaveVendorData(BackOfficeSignInRequest signInRequest) {
        BackOfficeUserData userData = createBackOfficeUserData(signInRequest);
        userData.setUserCategory(UserCategory.VENDOR);

        VendorData vendorData = new VendorData();
        BeanUtils.copyProperties(signInRequest.getVendorRequest(), vendorData);

        vendorData.setVendorAgentId(RandomStringUtils.randomAlphanumeric(8));
        vendorRepository.save(vendorData);

        userData.setVendorAgentId(vendorData.getVendorAgentId());
        return userData;
    }

    private BackOfficeUserData createBackOfficeUserData(BackOfficeSignInRequest signInRequest) {
        BackOfficeUserData adminData = new BackOfficeUserData();
        BeanUtils.copyProperties(signInRequest, adminData);

        Address address = new Address();
        BeanUtils.copyProperties(signInRequest.getAddress(), address);
        address.setAddressId(UUID.randomUUID().toString());
        addressRepository.save(address);

        adminData.setUserId(UUID.randomUUID().toString());
        adminData.setPassword(bCryptPasswordEncoder.encode(signInRequest.getPassword()));
        adminData.setAddressId(address.getAddressId());

        return adminData;
    }

    private BackOfficeUserResponse createUserResponseFromData(BackOfficeUserData userData) {
        BackOfficeUserResponse userResponse = new BackOfficeUserResponse();
        BeanUtils.copyProperties(userResponse, userData);
        return userResponse;
    }
}
