package com.forexbot.api.service.impl;

import com.forexbot.api.dao.backoffice.address.Address;
import com.forexbot.api.dao.backoffice.address.AddressRequest;
import com.forexbot.api.dao.backoffice.*;
import com.forexbot.api.dao.backoffice.userdata.*;
import com.forexbot.api.dao.backoffice.vendor.VendorData;
import com.forexbot.api.dao.backoffice.vendor.VendorRequest;
import com.forexbot.api.repository.AddressRepository;
import com.forexbot.api.repository.BackOfficeRepository;
import com.forexbot.api.repository.VendorRepository;
import com.forexbot.api.service.BackOfficeService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

import static com.forexbot.api.web.utils.Constants.ZONE;

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
        BackOfficeUserData userData = backOfficeRepository.getBackOfficeUserDataByEmailIdAndActive(loginRequest.getEmailId(), true);

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
    public BackOfficeUserData createAdmin(BackOfficeSignInRequest signInRequest) throws IllegalAccessException {
        BackOfficeUserData userData = createAndSaveUserData(signInRequest, UserCategory.ADMIN);
        return backOfficeRepository.save(userData);
    }

    @Override
    @Transactional
    public BackOfficeUserData createVendor(BackOfficeSignInRequest signInRequest) throws IllegalAccessException {
        BackOfficeUserData userData = createAndSaveUserData(signInRequest, UserCategory.VENDOR);
        return backOfficeRepository.save(userData);
    }

    @Override
    public List<BackOfficeUserResponse> getAllUsers() {
        List<BackOfficeUserData> userDataList = backOfficeRepository.findAll();
        userDataList.removeIf(backOfficeUserData -> !backOfficeUserData.isActive());
        return userDataList
                .stream()
                .map(this::createUserResponseFromData)
                .collect(Collectors.toList());
    }

    @Override
    public BackOfficeUserResponse getUserByUserId(String userId) {
        BackOfficeUserData userData = backOfficeRepository.getBackOfficeUserDataByUserId(userId);
        return createUserResponseFromData(userData);
    }

    @Override
    @Transactional
    public BackOfficeUserResponse update(BackOfficeSignInRequest signInRequest, String userId) throws IllegalAccessException {
        if (!backOfficeRepository.existsByUserId(signInRequest.getCreatedBy())) {
            throw new IllegalAccessException("User trying to update this record not found in Admin");
        }
        BackOfficeUserData userData = updateUserData(signInRequest, userId);
        return createUserResponseFromData(userData);
    }

    @Override
    @Transactional
    public void deleteUser(String userId, String deletedBy) {
        BackOfficeUserData userData = backOfficeRepository.getBackOfficeUserDataByUserId(userId);
        userData.setActive(false);
        userData.setDeletedBy(deletedBy);
        backOfficeRepository.save(userData);
    }

    private boolean checkPasswordsMatch(String enteredPassword, String passwordFromDb) {
        return bCryptPasswordEncoder.matches(enteredPassword, passwordFromDb);
    }

    private boolean isOldDataActive(BackOfficeSignInRequest signInRequest) {

        BackOfficeUserData userData = backOfficeRepository
                .getBackOfficeUserDataByUserNameAndEmailIdAndMobileNum(signInRequest.getUserName(),
                                                                       signInRequest.getEmailId(),
                                                                       signInRequest.getMobileNum());
        if (null != userData) {
            return userData.isActive();
        }
        return false;
    }

    private BackOfficeUserData createAndSaveUserData(BackOfficeSignInRequest signInRequest, UserCategory userCategory) throws IllegalAccessException {
        if (isOldDataActive(signInRequest)) {
            throw new IllegalAccessException("Old Data is still active for email and mobileNum");
        }

        BackOfficeUserData userData = new BackOfficeUserData();
        BeanUtils.copyProperties(signInRequest, userData, "password");

        userData.setUserCategory(userCategory);
        userData.setUserId(UUID.randomUUID().toString());
        userData.setPassword(
                bCryptPasswordEncoder.encode(signInRequest.getPassword())
        );
        userData.setCreateDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
        userData.setHexData(String.valueOf(Hex.encode(signInRequest.getPassword().getBytes())));

        if (UserCategory.VENDOR.equals(userCategory)) {
            userData.setVendorAgentId(
                    createAndSaveVendor(signInRequest.getVendorRequest())
            );
        }
        userData.setAddressId(
                createAndSaveAddress(signInRequest.getAddress())
        );

        return userData;
    }

    private String createAndSaveAddress(AddressRequest request) {
        Address address = new Address();
        BeanUtils.copyProperties(request, address);

        address.setAddressId(UUID.randomUUID().toString());
        addressRepository.save(address);

        return address.getAddressId();
    }

    private String createAndSaveVendor(VendorRequest request) {
        VendorData vendorData = new VendorData();
        BeanUtils.copyProperties(request, vendorData);

        vendorData.setVendorAgentId(RandomStringUtils.randomAlphanumeric(8));
        vendorRepository.save(vendorData);

        return vendorData.getVendorAgentId();
    }

    private BackOfficeUserData updateUserData(BackOfficeSignInRequest signInRequest, String userId) {
        BackOfficeUserData userData = backOfficeRepository.getBackOfficeUserDataByUserId(userId);
        BeanUtils.copyProperties(signInRequest, userData, "password", "userCategory", "userId");
        userData.setModifiedDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());

        if (UserCategory.VENDOR.equals(userData.getUserCategory())) {
            VendorData vendorData = vendorRepository.getVendorDataByVendorAgentId(userData.getVendorAgentId());
            BeanUtils.copyProperties(signInRequest.getVendorRequest(), vendorData, "vendorAgentId");
            vendorRepository.save(vendorData);
        }
        Address address = addressRepository.getAddressByAddressId(userData.getAddressId());
        BeanUtils.copyProperties(signInRequest.getAddress(), address, "addressId");
        addressRepository.save(address);

        return backOfficeRepository.save(userData);
    }

    private BackOfficeUserResponse createUserResponseFromData(BackOfficeUserData userData) {
        BackOfficeUserResponse userResponse = new BackOfficeUserResponse();
        BeanUtils.copyProperties(userData, userResponse, "password");
        userResponse.setAddress(addressRepository.getAddressByAddressId(userData.getAddressId()));
        userResponse.setVendorData(vendorRepository.getVendorDataByVendorAgentId(userData.getVendorAgentId()));
        userResponse.setPassword(new String(Hex.decode(userData.getHexData())));
        return userResponse;
    }
}
