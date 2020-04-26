package com.example.demo.service;

import com.example.demo.repository.dao.userdata.UserData;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;

public interface EmailService {

    enum EmailType {
        WELCOME,
        RESET
    }

    void sendEmail(String emailId, UserData userData, EmailType type) throws MessagingException, IOException, URISyntaxException;

    void sendEmail(String emailId, EmailType emailType) throws MessagingException, IOException, URISyntaxException;

    boolean doLookup(String emailId);
}
