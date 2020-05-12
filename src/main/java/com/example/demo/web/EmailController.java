package com.example.demo.web;

import com.example.demo.repository.dao.ResponseWrapper;
import com.example.demo.repository.dao.otp.OtpRequest;
import com.example.demo.repository.dao.userdata.UserData;
import com.example.demo.repository.dao.userdata.UserDataRequest;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserDataService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import java.io.IOException;

import static com.example.demo.web.utils.Constants.ERROR;
import static com.example.demo.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("api/v1")
public class EmailController {

    private EmailService emailService;
    private UserDataService userDataService;

    public EmailController(EmailService emailService, UserDataService userDataService) {
        this.emailService = emailService;
        this.userDataService = userDataService;
    }

    @PostMapping(path = "/sendResetEmail")
    public ResponseEntity<ResponseWrapper> sendResetEmail(@RequestBody UserDataRequest userRequest) throws IOException, MessagingException {
        return sendEmail(userRequest.getEmailId(), EmailService.EmailType.RESET);
    }

    @PostMapping(path = "/sendWelcomeEmail")
    public ResponseEntity<ResponseWrapper> sendWelcomeEmail(@RequestBody UserDataRequest userRequest) throws IOException, MessagingException {
        return sendEmail(userRequest.getEmailId(), EmailService.EmailType.WELCOME);
    }

    @PostMapping(path = "/sendVerificationMail")
    public ResponseEntity<ResponseWrapper> sendVerificationMail(@RequestBody UserDataRequest userDataRequest) throws IOException, MessagingException {
        return sendEmail(userDataRequest.getEmailId(), EmailService.EmailType.VERIFY);
    }

    @PostMapping(path = "/sendOtp")
    public ResponseEntity<ResponseWrapper> sendOtp(@RequestBody OtpRequest otpRequest) {
        String otp = userDataService.generateAndSaveOtp(otpRequest);
        try {
            emailService.sendOtp(otpRequest.getMobileNum(), otp);
        } catch (IOException e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    e.getMessage(),
                    null), HttpStatus.OK);
        }

        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Otp sent to number: " + otpRequest.getMobileNum(),
                null), HttpStatus.OK);
    }

    @PostMapping(path = "/verifyOtp")
    public ResponseEntity<ResponseWrapper> verifyOtp(@RequestBody OtpRequest otpRequest) {
        // TODO: Use mobile Num after SMS impl
        try {
            if (userDataService.verifyOtp(otpRequest)) {
                return new ResponseEntity<>(new ResponseWrapper(
                        SUCCESS,
                        "Otp verification successful",
                        null), HttpStatus.OK);
            }
        } catch (IllegalAccessException e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    e.getMessage(),
                    null), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                ERROR,
                "Invalid OTP",
                null), HttpStatus.OK);
    }

    @PostMapping(path = "/verifyEmail")
    public ResponseEntity<ResponseWrapper> verifyEmail(@RequestBody UserDataRequest userDataRequest) {
        try {
            userDataService.verifyEmail(userDataRequest);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    e.getMessage(),
                    null), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Email verified successfully",
                null), HttpStatus.OK);
    }

    private ResponseEntity<ResponseWrapper> sendEmail(String emailId, EmailService.EmailType emailType) throws IOException, MessagingException {
        UserData userData = userDataService.getUserDataByEmailId(emailId);

        if (ObjectUtils.isEmpty(userData)) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "No User registered with email " + emailId,
                    null), HttpStatus.OK);
        }

        emailService.sendEmail(emailId, userData, emailType);
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Email sent successfully",
                null), HttpStatus.OK);
    }
}