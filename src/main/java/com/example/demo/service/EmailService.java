package com.example.demo.service;

import com.example.demo.repository.dao.userdata.UserData;

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

    // TODO: Remove after sms implemented
    void sendOtpEmail(String emailId, String otp) throws MessagingException, IOException;
}
