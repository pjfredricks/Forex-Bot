package com.forexbot.api.service;


import com.forexbot.api.dao.backoffice.userdata.*;

import java.util.List;

public interface BackOfficeService {
    BackOfficeLoginResponse login(BackOfficeLoginRequest loginRequest) throws Exception;

    BackOfficeUserData createAdmin(BackOfficeSignInRequest signInRequest) throws IllegalAccessException;

    BackOfficeUserData createVendor(BackOfficeSignInRequest signInRequest) throws IllegalAccessException;

    List<BackOfficeUserResponse> getAllUsers();

    BackOfficeUserResponse getUserByUserId(String userId);

    BackOfficeUserResponse update(BackOfficeSignInRequest signInRequest, String userId) throws IllegalAccessException;

    void deleteUser(String userId, String deletedBy);
}
