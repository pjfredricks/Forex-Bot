package com.example.demo.service.impl;

import com.example.demo.repository.UserDataRepository;
import com.example.demo.repository.OtpDataRepository;
import com.example.demo.repository.dao.otp.OtpData;
import com.example.demo.repository.dao.otp.OtpRequest;
import com.example.demo.repository.dao.otp.OtpType;
import com.example.demo.repository.dao.userdata.*;
import com.example.demo.service.UserDataService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

import static com.example.demo.web.utils.Constants.ZONE;

@Service
public class UserDataServiceImpl implements UserDataService {

    private OtpDataRepository otpDataRepository;
    private UserDataRepository userDataRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDataServiceImpl(UserDataRepository userDataRepository,
                               BCryptPasswordEncoder bCryptPasswordEncoder,
                               OtpDataRepository otpDataRepository) {
        this.userDataRepository = userDataRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.otpDataRepository = otpDataRepository;
    }

    @Override
    @Transactional
    public UserDataResponse signUpUser(UserDataRequest userDataRequest) {
        UserData userData = mapRequestToData(userDataRequest);
        OtpData otpData = otpDataRepository.findOtpDataByEmailId(userDataRequest.getEmailId());
        if (ObjectUtils.isNotEmpty(otpData) && otpData.isOtpVerified()) {
            userData.setMobileVerified(true);
        }
        userDataRepository.save(userData);

        return mapDataToResponse(userData);
    }

    @Override
    public UserDataResponse login(UserDataRequest userDataRequest) {
        UserData userData = null;

        if (userDataRequest.getEmailId().contains("@")) {
            userData = getUserDataByEmailId(userDataRequest.getEmailId());
        } else {
            userData = getUserDataByMobileNum(userDataRequest.getEmailId());
        }

        if (checkPasswordsMatch(userDataRequest.getPassword(), userData.getPassword())) {
            return mapDataToResponse(userData);
        }
        return null;
    }

    @Override
    public UserData getUserDetailsById(UUID userId) {
        return userDataRepository.getUserDataByUserId(userId);
    }

    public UserDataResponse resetPassword(UserDataRequest resetRequest) throws IllegalAccessException {
        UserData userData = userDataRepository.getUserDataByUserId(UUID.fromString(resetRequest.getUserId()));

        if (ObjectUtils.isNotEmpty(userData)) {
            userData.setPassword(bCryptPasswordEncoder.encode(resetRequest.getPassword()));
            userData.setModifiedDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
            userDataRepository.save(userData);
            return mapDataToResponse(userData);
        }

        throw new IllegalAccessException("Bad Credentials, Password reset failed");
    }

    @Override
    public UserData getUserDataByEmailId(String emailId) {
        return userDataRepository.getUserDataByEmailId(emailId);
    }

    @Override
    public UserData getUserDataByMobileNum(String mobileNum) {
        return userDataRepository.getUserDataByMobileNum(mobileNum);
    }

    @Override
    @Transactional
    public UserDataResponse updateUserDetails(UserDataRequest updateRequest) throws IllegalAccessException {
        UserData userData = userDataRepository.getUserDataByUserId(UUID.fromString(updateRequest.getUserId()));

        if (ObjectUtils.isNotEmpty(userData)) {
            if (userData.isEmailVerified()) {
                throw new IllegalAccessException("EmailId is verified, Not allowed to update");
            }
            if (userData.isMobileVerified()) {
                throw new IllegalAccessException("MobileNum is verified, Not allowed to update");
            }
            if (StringUtils.isNotBlank(updateRequest.getName())) {
                userData.setName(updateRequest.getName());
            }
            if (StringUtils.isNotBlank(updateRequest.getEmailId())) {
                userData.setEmailId(updateRequest.getEmailId());
            }
            if (StringUtils.isNotBlank(updateRequest.getMobileNum())) {
                userData.setMobileNum(updateRequest.getMobileNum());
            }
            userData.setModifiedDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
            userDataRepository.save(userData);
            return mapDataToResponse(userData);
        }

        throw new IllegalAccessException("Not able to find records for requested details");
    }

    @Override
    @Transactional
    public String generateAndSaveOtp(OtpRequest otpRequest) {
        String otp = RandomStringUtils.randomNumeric(6);
        OtpData otpData = otpDataRepository.findOtpDataByEmailIdAndOtpType(otpRequest.getMobileNum(),
                OtpType.valueOf(otpRequest.getOtpType()));

        if (ObjectUtils.isNotEmpty(otpData)) {
            otpData.setOtp(bCryptPasswordEncoder.encode(otp));
            otpData.setModifiedDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
            otpData.setRetryCount(otpData.getRetryCount() + 1);
        } else {
            otpData = new OtpData();
            if (!OtpType.SIGN_UP.equals(OtpType.valueOf(otpRequest.getOtpType()))) {
                otpData.setUserId(UUID.fromString(otpRequest.getUserId()));
            }
            otpData.setOtp(bCryptPasswordEncoder.encode(otp));
            otpData.setEmailId(otpRequest.getMobileNum());
            otpData.setOtpType(OtpType.valueOf(otpRequest.getOtpType()));
            otpData.setCreateDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
            otpData.setRetryCount(0);
        }

        otpDataRepository.save(otpData);
        return otp;
    }

    @Override
    @Transactional
    public boolean verifyOtp(OtpRequest otpRequest) throws IllegalAccessException {
        OtpData otpData = otpDataRepository.findOtpDataByEmailIdAndOtpType(otpRequest.getMobileNum(),
                OtpType.valueOf(otpRequest.getOtpType()));

        if (ObjectUtils.isEmpty(otpData)) {
            throw new IllegalAccessException("Invalid emailId or Otp Type");
        }

        if (otpRequest.getOtpType() != 0) {
            if (ObjectUtils.isEmpty(userDataRepository.getUserDataByUserId(UUID.fromString(otpRequest.getUserId())))) {
                throw new IllegalAccessException("No user found for userId: " + otpRequest.getUserId());
            }
        }

        if (bCryptPasswordEncoder.matches(otpRequest.getOtp(), otpData.getOtp())) {
            otpData.setOtpVerified(true);
            otpDataRepository.save(otpData);
            return true;
        }

        return false;
    }

    @Override
    @Transactional
    public void verifyEmail(UserDataRequest userDataRequest) throws IllegalAccessException {
        UserData userData = userDataRepository.getUserDataByUserId(UUID.fromString(userDataRequest.getUserId()));
        if (ObjectUtils.isEmpty(userData)) {
            throw new IllegalAccessException("No user fround for userId: " + userDataRequest.getUserId());
        }
        userData.setEmailVerified(true);
        userDataRepository.save(userData);
    }

    private boolean checkPasswordsMatch(String enteredPassword, String passwordFromDb) {
        return bCryptPasswordEncoder.matches(enteredPassword, passwordFromDb);
    }

    private UserData mapRequestToData(UserDataRequest userRequest) {
        UserData userData = new UserData();

        userData.setUserId(UUID.randomUUID());
        userData.setEmailId(userRequest.getEmailId());
        userData.setMobileNum(userRequest.getMobileNum());
        userData.setName(userRequest.getName());
        userData.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        userData.setCreateDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
        return userData;
    }

    private UserDataResponse mapDataToResponse(UserData userData) {
        UserDataResponse response = new UserDataResponse();

        response.setUserId(userData.getUserId().toString());
        response.setEmailId(userData.getEmailId());
        response.setMobileNum(userData.getMobileNum());
        response.setName(userData.getName());
        return response;
    }
}