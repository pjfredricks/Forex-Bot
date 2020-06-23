package com.forexbot.api.service;

import com.forexbot.api.dao.admin.*;

import java.util.List;

public interface BackOfficeService {
    BackOfficeLoginResponse login(BackOfficeLoginRequest loginRequest) throws Exception;

    BackOfficeUserData createAdmin(BackOfficeSignInRequest signInRequest);

    BackOfficeUserData createVendor(BackOfficeSignInRequest signInRequest);

    List<BackOfficeUserResponse> getAllUsers();
}
