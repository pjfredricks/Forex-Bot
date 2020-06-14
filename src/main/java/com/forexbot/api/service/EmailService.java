package com.forexbot.api.service;

import com.forexbot.api.dao.userdata.UserData;

import javax.mail.MessagingException;
import java.io.IOException;

public interface EmailService {

    enum EmailType {
        WELCOME,
        RESET,
        VERIFY,
        BACKUP
    }

    void sendEmail(String emailId, UserData userData, EmailType emailType) throws MessagingException, IOException;
}
