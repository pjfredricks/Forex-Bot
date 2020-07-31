package com.forexbot.api.service.impl;

import com.forexbot.api.dao.otp.OtpData;
import com.forexbot.api.dao.otp.OtpRequest;
import com.forexbot.api.dao.otp.OtpType;
import com.forexbot.api.dao.userdata.UserData;
import com.forexbot.api.dao.userdata.UserDataRequest;
import com.forexbot.api.dao.userdata.UserDataResponse;
import com.forexbot.api.repository.OtpDataRepository;
import com.forexbot.api.repository.UserDataRepository;
import com.forexbot.api.service.UserDataService;
import org.apache.commons.lang3.ObjectUtils;
import org.apache.commons.lang3.RandomStringUtils;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.codec.Hex;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.List;
import java.util.UUID;

import static com.forexbot.api.web.utils.Constants.ZONE;

@Service
public class UserDataServiceImpl implements UserDataService {

    private final OtpDataRepository otpDataRepository;
    private final UserDataRepository userDataRepository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDataServiceImpl(UserDataRepository userDataRepository,
                               BCryptPasswordEncoder bCryptPasswordEncoder,
                               OtpDataRepository otpDataRepository) {
        this.userDataRepository = userDataRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
        this.otpDataRepository = otpDataRepository;
    }

    @Override
    public List<UserData> getAllUsers() {
        return userDataRepository.findAll();
    }

    @Override
    @Transactional
    public UserDataResponse signUpUser(UserDataRequest userDataRequest) {
        UserData userData = mapRequestToData(userDataRequest);
        OtpData otpData = otpDataRepository.findOtpDataByMobileNum(userDataRequest.getMobileNum());
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
            userData = getUserDataByEmailIdOrMobileNum(userDataRequest.getEmailId(), null);
        } else {
            userData = getUserDataByEmailIdOrMobileNum(null, userDataRequest.getEmailId());
        }

        if (ObjectUtils.isNotEmpty(userData) && checkPasswordsMatch(userDataRequest.getPassword(), userData.getPassword())) {
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
            userData.setHexData(String.valueOf(Hex.encode(resetRequest.getPassword().getBytes())));
            userData.setPassword(bCryptPasswordEncoder.encode(resetRequest.getPassword()));
            userData.setModifiedDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
            userDataRepository.save(userData);
            return mapDataToResponse(userData);
        }

        throw new IllegalAccessException("Bad Credentials, Password reset failed");
    }

    @Override
    public UserData getUserDataByEmailIdOrMobileNum(String emailId, String mobileNum) {
        return userDataRepository.getUserDataByEmailIdOrMobileNum(emailId, mobileNum);
    }

    @Override
    @Transactional
    public UserDataResponse updateUserDetails(UserDataRequest updateRequest) throws IllegalAccessException {
        UserData userData = userDataRepository.getUserDataByUserId(UUID.fromString(updateRequest.getUserId()));

        if (ObjectUtils.isNotEmpty(userData)) {
            if (StringUtils.isNotBlank(updateRequest.getEmailId()) && userData.isEmailVerified()) {
                throw new IllegalAccessException("EmailId is verified, Not allowed to update");
            } else {
                userData.setEmailId(updateRequest.getEmailId());
            }
            if (StringUtils.isNotBlank(updateRequest.getMobileNum()) && userData.isMobileVerified()) {
                throw new IllegalAccessException("MobileNum is verified, Not allowed to update");
            } else {
                userData.setMobileNum(updateRequest.getMobileNum());
            }
            if (StringUtils.isNotBlank(updateRequest.getName())) {
                userData.setName(updateRequest.getName());
            }
            userData.setModifiedDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
            userDataRepository.save(userData);
            return mapDataToResponse(userData);
        }

        throw new IllegalAccessException("Not able to find records for requested details");
    }

    @Override
    @Transactional
    public String generateAndSaveOtp(OtpRequest otpRequest) throws IllegalAccessException {
        String otp = RandomStringUtils.randomNumeric(6);
        isNewUser(otpRequest);

        OtpData otpData = otpDataRepository.findOtpDataByMobileNumAndOtpType(otpRequest.getMobileNum(),
                OtpType.valueOf(otpRequest.getOtpType()));

        otpData = constructOtpData(otpData, otp, otpRequest);

        otpDataRepository.save(otpData);
        return otp;
    }

    @Override
    @Transactional
    public boolean verifyOtp(OtpRequest otpRequest) throws IllegalAccessException {
        OtpData otpData = otpDataRepository.findOtpDataByMobileNumAndOtpType(otpRequest.getMobileNum(),
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

    private void isNewUser(OtpRequest otpRequest) throws IllegalAccessException {
        if (OtpType.SIGN_UP.equals(OtpType.valueOf(otpRequest.getOtpType()))
                && (ObjectUtils.isNotEmpty(userDataRepository.getUserDataByMobileNum(otpRequest.getMobileNum()))
                || ObjectUtils.isNotEmpty(userDataRepository.getUserDataByEmailId(otpRequest.getEmailId())))) {
            throw new IllegalAccessException("User already registered with credentials");
        }
    }

    private OtpData constructOtpData(OtpData otpData, String otp, OtpRequest otpRequest) {
        if (ObjectUtils.isNotEmpty(otpData)) {
            otpData.setOtp(bCryptPasswordEncoder.encode(otp));
            otpData.setModifiedDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
            otpData.setRetryCount(otpData.getRetryCount() + 1);
        } else {
            otpData = new OtpData();
            otpData.setOtp(bCryptPasswordEncoder.encode(otp));
            otpData.setMobileNum(otpRequest.getMobileNum());
            otpData.setOtpType(OtpType.valueOf(otpRequest.getOtpType()));
            otpData.setCreateDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
            otpData.setRetryCount(0);
        }

        return otpData;
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
        userData.setHexData(String.valueOf(Hex.encode(userRequest.getPassword().getBytes())));
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