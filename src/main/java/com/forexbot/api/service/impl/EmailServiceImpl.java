package com.forexbot.api.service.impl;

import com.forexbot.api.dao.userdata.UserData;
import com.forexbot.api.repository.UserDataRepository;
import com.forexbot.api.service.EmailService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {

    private final JavaMailSender javaMailSender;
    private final UserDataRepository userDataRepository;

    public EmailServiceImpl(JavaMailSender javaMailSender, UserDataRepository userDataRepository) {
        this.javaMailSender = javaMailSender;
        this.userDataRepository = userDataRepository;
    }

    @Override
    public void sendEmail(String emailId, EmailType type) throws MessagingException, IOException, IllegalAccessException {
        UserData userData = userDataRepository.getUserDataByEmailId(emailId);

        if (ObjectUtils.isEmpty(userData)) {
            throw new IllegalAccessException("No User registered with email " + emailId);
        }

        sendEmailByType(emailId, userData, type);
    }

    @Async
    void sendEmailByType(String emailId, UserData userData, EmailType type) throws IOException, MessagingException {
        switch (type) {
            case RESET:
                javaMailSender.send(constructMail(emailId, userData, "email/reset_pass.html", "Reset your Forex Bot password"));
                break;
            case WELCOME:
                javaMailSender.send(constructMail(emailId, userData, "email/welcome_page.html", "Welcome to ForexBot"));
                break;
            case VERIFY:
                javaMailSender.send(constructMail(emailId, userData, "email/verify_email.html", "Verify your Email address"));
                break;
            case CONFIRM:
                javaMailSender.send(constructMail(emailId, userData, "email/order_confirm.html", "Your order has been confirmed"));
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
        message.setFrom(new InternetAddress("info@forexbot.in", "Forex Bot"));

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(emailId);
        helper.setSubject(subject);

        String pageAsText = new String(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)).readAllBytes(), StandardCharsets.UTF_8);
        pageAsText = pageAsText.replace("{userName}", user.getName());
        pageAsText = pageAsText.replace("{userId}", user.getUserId().toString());
        helper.setText(pageAsText, true);

        return message;
    }
}
