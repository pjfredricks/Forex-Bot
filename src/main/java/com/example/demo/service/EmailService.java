package com.example.demo.service;

import javax.mail.MessagingException;
import java.io.IOException;
import java.net.URISyntaxException;

public interface EmailService {

    enum EmailType {
        WELCOME,
        RESET,
        BACKUP
    }

    void sendEmail(String emailId, EmailType emailType) throws MessagingException, IOException, URISyntaxException, IllegalAccessException;
}
