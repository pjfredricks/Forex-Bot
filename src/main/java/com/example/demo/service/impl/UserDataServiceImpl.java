package com.example.demo.service.impl;

import com.example.demo.repository.UserDataRepository;
import com.example.demo.repository.dao.UserData.UserData;
import com.example.demo.repository.dao.UserData.UserDataRequest;
import com.example.demo.repository.dao.UserData.UserDataResponse;
import com.example.demo.service.UserDataService;
import org.apache.commons.lang3.StringUtils;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import javax.transaction.Transactional;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.util.UUID;

@Service
public class UserDataServiceImpl implements UserDataService {

    private UserDataRepository userDataRepository;
    private BCryptPasswordEncoder bCryptPasswordEncoder;

    public UserDataServiceImpl(UserDataRepository userDataRepository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.userDataRepository = userDataRepository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @Override
    @Transactional
    public UserDataResponse signUpUser(UserDataRequest userDataRequest) {
        UserData userData = mapRequestToData(userDataRequest);
        userData.setId(userDataRepository.save(userData).getId());

        return mapDataToResponse(userData);
    }

    @Override
    public UserDataResponse login(UserDataRequest userDataRequest) throws Exception {
        UserData userData = null;

        if (StringUtils.isEmpty(userDataRequest.getName())) {
            userData = getUserDataByEmailIdOrMobileNum(userDataRequest.getEmailId(), userDataRequest.getMobileNum());
        } else {
            userData = getUserDataByName(userDataRequest.getName());
        }

        if (checkPasswordsMatch(userDataRequest.getPassword(), userData.getPassword())) {
            return mapDataToResponse(userData);
        }
        return null;
    }

    private UserData getUserDataByName(String userName) {
        return userDataRepository.getUserDataByName(userName);
    }

    private UserData getUserDataByEmailIdOrMobileNum(String emailId, String mobileNum) {
        return userDataRepository.getUserDataByEmailIdOrMobileNum(emailId, mobileNum);
    }

    private Boolean checkPasswordsMatch(String enteredPassword, String passwordFromDb) {
        return bCryptPasswordEncoder.matches(enteredPassword, passwordFromDb);
    }

    private UserData mapRequestToData(UserDataRequest userRequest) {
        UserData userData = new UserData();

        userData.setUserId(UUID.randomUUID());
        userData.setEmailId(userRequest.getEmailId());
        userData.setMobileNum(userRequest.getMobileNum());
        userData.setName(userRequest.getName());
        userData.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        userData.setCreate_date(LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toString());
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

    // TODO: add update password
    private UserData updatePassword(UserDataRequest userDataRequest) {
        UserData userDataFromDb = StringUtils.isEmpty(userDataRequest.getName())
                ? getUserDataByEmailIdOrMobileNum(userDataRequest.getEmailId(), null) : getUserDataByName(userDataRequest.getName());

        if (checkPasswordsMatch(userDataRequest.getPassword(), userDataFromDb.getPassword())) {
            // Executed only when user entered password and db password match
            userDataFromDb.setPassword(bCryptPasswordEncoder.encode(userDataRequest.getPassword()));
            userDataFromDb.setModified_date(LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toString());
            return userDataFromDb;
        } else {
            // UserData password does not match
            return null;
        }
    }
}