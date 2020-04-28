package com.example.demo.service.impl;

import com.example.demo.repository.dao.userdata.UserData;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserDataService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.net.URISyntaxException;
import java.nio.charset.StandardCharsets;
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
    @Async
    public void sendEmail(String emailId, EmailType type) throws MessagingException, IOException, URISyntaxException, IllegalAccessException {
        UserData userData = userDataService.getUserDataByEmailIdOrMobileNum(emailId, null);

        if (userData == null) {
            throw new IllegalAccessException("No user found for EmailId " + emailId);
        }

        sendEmail(emailId, userData, type);
    }

    private void sendEmail(String emailId, UserData userData, EmailType type) throws MessagingException, IOException, URISyntaxException {
        switch (type) {
            case RESET:
                javaMailSender.send(constructMail(emailId, userData, "reset_pass.html", "Reset your Forext Bot password"));
                break;
            case WELCOME:
                javaMailSender.send(constructMail(emailId, userData, "welcome_page.html", "Welcome to ForexBot"));
                break;
            default:
                break;
        }
    }

    private MimeMessage constructMail(String emailId,
                                      UserData user,
                                      String fileName,
                                      String subject) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(emailId);
        helper.setSubject(subject);

        String pageAsText = new String(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName).readAllBytes(), StandardCharsets.UTF_8);
        pageAsText = pageAsText.replace("{userName}", user.getName());
        pageAsText = pageAsText.replace("{userId}", user.getUserId().toString());
        helper.setText(pageAsText, true);

        return message;
    }
}
