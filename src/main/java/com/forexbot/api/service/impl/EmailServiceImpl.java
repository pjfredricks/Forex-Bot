package com.forexbot.api.service.impl;

import com.forexbot.api.dao.customer.CustomerData;
import com.forexbot.api.repository.CustomerRepository;
import com.forexbot.api.service.EmailService;
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
    private final CustomerRepository customerRepository;

    public EmailServiceImpl(JavaMailSender javaMailSender, CustomerRepository customerRepository) {
        this.javaMailSender = javaMailSender;
        this.customerRepository = customerRepository;
    }

    @Override
    public void sendEmail(String emailId, EmailType type) throws MessagingException, IOException, IllegalAccessException {
        CustomerData customerData = customerRepository.getCustomerDataByEmailId(emailId);

        if (null == customerData) {
            throw new IllegalAccessException("No User registered with email " + emailId);
        }

        sendEmailByType(emailId, customerData, type);
    }

    @Async
    void sendEmailByType(String emailId, CustomerData customerData, EmailType type) throws IOException, MessagingException {
        switch (type) {
            case RESET:
                javaMailSender.send(constructMail(emailId, customerData, "email/reset_pass.html", "Reset your Forex Bot password"));
                break;
            case WELCOME:
                javaMailSender.send(constructMail(emailId, customerData, "email/welcome_page.html", "Welcome to ForexBot"));
                break;
            case VERIFY:
                javaMailSender.send(constructMail(emailId, customerData, "email/verify_email.html", "Verify your Email address"));
                break;
            case CONFIRM:
                javaMailSender.send(constructMail(emailId, customerData, "email/order_confirm.html", "Your order has been confirmed"));
                break;
            default:
                break;
        }
    }

    private MimeMessage constructMail(String emailId,
                                      CustomerData customer,
                                      String fileName,
                                      String subject) throws MessagingException, IOException {
        MimeMessage message = javaMailSender.createMimeMessage();
        message.setFrom(new InternetAddress("info@forexbot.in", "Forex Bot"));

        MimeMessageHelper helper = new MimeMessageHelper(message, true);
        helper.setTo(emailId);
        helper.setSubject(subject);

        String pageAsText = new String(Objects.requireNonNull(Thread.currentThread().getContextClassLoader().getResourceAsStream(fileName)).readAllBytes(), StandardCharsets.UTF_8);
        pageAsText = pageAsText.replace("{userName}", customer.getName());
        pageAsText = pageAsText.replace("{userId}", customer.getCustomerId().toString());
        helper.setText(pageAsText, true);

        return message;
    }
}
