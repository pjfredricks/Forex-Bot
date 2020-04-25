package com.example.demo.service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;

public interface EmailService {

    enum EmailType {
        WELCOME,
        RESET
    }

    void sendEmail(String emailId, String userName, EmailType type) throws MessagingException, IOException, URISyntaxException;

    void sendEmail(String emailId, EmailType emailType) throws MessagingException, IOException, URISyntaxException;

    boolean doLookup(String emailId);
}
