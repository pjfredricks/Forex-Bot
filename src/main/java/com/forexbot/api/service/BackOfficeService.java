package com.forexbot.api.service;

import com.forexbot.api.dao.admin.*;

public interface BackOfficeService {
    BackOfficeLoginResponse login(BackOfficeLoginRequest loginRequest) throws Exception;

    BackOfficeUserData createAdmin(BackOfficeSignInRequest signInRequest);

    BackOfficeUserData createVendor(BackOfficeSignInRequest signInRequest);
}
