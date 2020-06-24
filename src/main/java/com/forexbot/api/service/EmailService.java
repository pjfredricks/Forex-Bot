package com.forexbot.api.service;

import com.forexbot.api.dao.userdata.UserData;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

    enum EmailType {
        WELCOME,
        RESET,
        VERIFY,
        CONFIRM,
        BACKUP
    }

    void sendEmail(String emailId, EmailType emailType) throws MessagingException, IOException, IllegalAccessException;
}
