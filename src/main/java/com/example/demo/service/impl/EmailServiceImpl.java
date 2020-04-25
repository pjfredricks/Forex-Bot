package com.example.demo.service.impl;

import com.example.demo.repository.dao.userdata.UserData;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserDataService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.file.Files;
import java.nio.file.Paths;
import java.util.Set;

@Service
public class EmailServiceImpl implements EmailService {

    private static final Set<String> allowedDns = Set.of("@gmail", "@outlook", "@yahoo", "@rediff", "@forex");

    private JavaMailSender javaMailSender;
    private UserDataService userDataService;

    public EmailServiceImpl(JavaMailSender javaMailSender, UserDataService userDataService) {
        this.javaMailSender = javaMailSender;
        this.userDataService = userDataService;
    }

    @Override
    public void sendEmail(String emailId, String userName, EmailType type) throws MessagingException, IOException, URISyntaxException {
        switch (type) {
            case RESET:
                javaMailSender.send(constructMail(emailId, userName, "reset_pass.html", "Reset your Forext Bot password"));
                break;
            case WELCOME:
                javaMailSender.send(constructMail(emailId, userName, "welcome_page.html", "Welcome to ForexBot"));
                break;
            default:
                break;
        }
    }


    @Override
    public void sendEmail(String emailId, EmailType type) throws MessagingException, IOException, URISyntaxException {
        UserData userData = userDataService.getUserDataByEmailIdOrMobileNum(emailId, null);

        if (userData == null) {
            throw new MessagingException("No user found for EmailId " + emailId);
        }

        sendEmail(emailId, userData.getName(), type);
    }

    @Override
    public boolean doLookup(String emailId) {
        return allowedDns.stream().anyMatch(emailId::contains);
    }

    private MimeMessage constructMail(String emailId,
                                      String userName,
                                      String fileName,
                                      String subject) throws MessagingException, IOException, URISyntaxException {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(emailId);
        helper.setSubject(subject);

        String pageAsText = Files.readString(Paths.get(ClassLoader.getSystemResource(fileName).toURI()));
        pageAsText = pageAsText.replace("{userName}", userName);
        helper.setText(pageAsText, true);

        return message;
    }
}
