package com.example.demo.service.impl;

import com.example.demo.repository.OtpDataRepository;
import com.example.demo.repository.dao.otp.OtpData;
import com.example.demo.repository.dao.userdata.UserData;
import com.example.demo.service.EmailService;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.mail.javamail.MimeMessageHelper;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Service;

import javax.mail.MessagingException;
import javax.mail.internet.InternetAddress;
import javax.mail.internet.MimeMessage;
import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;
import java.net.HttpURLConnection;
import java.net.URL;
import java.nio.charset.StandardCharsets;
import java.util.Objects;

@Service
public class EmailServiceImpl implements EmailService {

    public static final String API_KEY = "apikey=y76sJhh/rDc-yHrVdirEXTy8BiOEi23hZKkiewMrcr";
    public static final String SENDER = "&sender=FEXBOT";
    public static final String OTP_MESSAGE = "&message=Your OTP for your Forex Bot transaction is ";
    public static final String ORDER_CONFIRM_MESSAGE = "&message=Your OTP for your Forex Bot transaction is";

    private JavaMailSender javaMailSender;
    private OtpDataRepository otpDataRepository;

    public EmailServiceImpl(JavaMailSender javaMailSender, OtpDataRepository otpDataRepository) {
        this.javaMailSender = javaMailSender;
        this.otpDataRepository = otpDataRepository;
    }

    @Override
    @Async
    public void sendEmail(String emailId, UserData userData, EmailType type) throws MessagingException, IOException {
        switch (type) {
            case RESET:
                javaMailSender.send(constructMail(emailId, userData, "reset_pass.html", "Reset your Forext Bot password"));
                break;
            case WELCOME:
                javaMailSender.send(constructMail(emailId, userData, "welcome_page.html", "Welcome to ForexBot"));
                break;
            case VERIFY:
                javaMailSender.send(constructMail(emailId, userData, "verify_email.html", "Verify your Email address"));
                break;
            default:
                break;
        }
    }

    @Override
    public void sendOtp(String mobileNum, String otp) throws IOException {
        String message = OTP_MESSAGE + otp;
        String numbers = "&numbers=" + mobileNum;
        String data = API_KEY + numbers + message + SENDER;

        sendSms(mobileNum, data);
    }

    @Override
    public void sendConfirmation(String mobileNum) throws IOException {
//        String message = "&message=" + "Your OTP for your Forex Bot transaction is " + otp;
//        String numbers = "&numbers=" + mobileNum;
//        String data = API_KEY + numbers + message + SENDER;
//
//        sendSms(mobileNum, data);
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

    private void sendSms(String mobileNum, String data) throws IOException {
        OtpData otpData = otpDataRepository.findOtpDataByMobileNumber(mobileNum);

        // Send data
        HttpURLConnection conn = (HttpURLConnection) new URL("https://api.textlocal.in/send/?").openConnection();
        conn.setDoOutput(true);
        conn.setRequestMethod("POST");
        conn.setRequestProperty("Content-Length", Integer.toString(data.length()));
        conn.getOutputStream().write(data.getBytes(StandardCharsets.UTF_8));
        final BufferedReader rd = new BufferedReader(new InputStreamReader(conn.getInputStream()));
        final StringBuilder builder = new StringBuilder();
        String line;
        while ((line = rd.readLine()) != null) {
            builder.append(line);
        }
        rd.close();

        otpData.setTextLocalResponse(builder.toString());
        otpDataRepository.save(otpData);
    }
}
