package com.forexbot.api.web;

import com.forexbot.api.dao.userdata.UserData;
import com.forexbot.api.dao.userdata.UserDataRequest;
import com.forexbot.api.service.EmailService;
import com.forexbot.api.service.UserDataService;
import com.forexbot.api.web.utils.ResponseWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import javax.mail.MessagingException;
import java.io.IOException;

import static com.forexbot.api.web.utils.Constants.ERROR;
import static com.forexbot.api.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("api/v1")
public class EmailController {

    private final EmailService emailService;
    private final UserDataService userDataService;

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

    @PostMapping(path = "/sendConfirmationMail")
    public ResponseEntity<ResponseWrapper> sendConfirmationMail(@RequestBody UserDataRequest userDataRequest) throws IOException, MessagingException {
        return sendEmail(userDataRequest.getEmailId(), EmailService.EmailType.CONFIRM);
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
        try {
            emailService.sendEmail(emailId, emailType);
        } catch (IllegalAccessException ex) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    ex.getMessage(),
                    null), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Email sent successfully",
                null), HttpStatus.OK);
    }
}