package com.forexbot.api.dao.backoffice.userdata;

import lombok.Data;

@Data
public class BackOfficeLoginRequest {
    private String emailId;
    private String password;
}
