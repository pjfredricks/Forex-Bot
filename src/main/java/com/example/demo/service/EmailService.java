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

    void sendOtp(String mobileNum, String otp) throws IOException;
}
