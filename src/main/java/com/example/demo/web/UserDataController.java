package com.example.demo.web;

import com.example.demo.repository.dao.ResponseWrapper;
import com.example.demo.repository.dao.userdata.UserDataRequest;
import com.example.demo.repository.dao.userdata.UserDataResponse;
import com.example.demo.service.EmailService;
import com.example.demo.service.UserDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.mail.MessagingException;
import javax.naming.NamingException;
import java.util.UUID;

import static com.example.demo.web.utils.Constants.ERROR;
import static com.example.demo.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("/api/v1")
public class UserDataController {

    private UserDataService userDataService;
    private EmailService emailService;

    public UserDataController(UserDataService userDataService, EmailService emailService) {
        this.userDataService = userDataService;
        this.emailService = emailService;
    }

    @PostMapping(path = "/signUp")
    public ResponseEntity<ResponseWrapper> signUpUser(@RequestBody UserDataRequest userDataRequest) throws NamingException {
        if (emailService.doLookup(userDataRequest.getEmailId())) {
            try {
                UserDataResponse response = userDataService.signUpUser(userDataRequest);
                emailService.sendEmail(userDataRequest.getEmailId(), EmailService.EmailType.WELCOME);
                return new ResponseEntity<>(new ResponseWrapper(
                        SUCCESS,
                        "User signed Up successfully",
                        response), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseWrapper(
                        ERROR,
                        "User details already exist for " + userDataRequest.getMobileNum() + " and " + userDataRequest.getEmailId(),
                        null), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseWrapper(
                ERROR,
                "Invalid EmailId: " + userDataRequest.getEmailId(),
                null), HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<ResponseWrapper> login(@RequestBody UserDataRequest userDataRequest) {
        UserDataResponse userDataResponse = null;
        try {
            userDataResponse = userDataService.login(userDataRequest);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    e.getMessage(),
                    null), HttpStatus.OK);
        }
        if (userDataResponse != null) {
                return new ResponseEntity<>(new ResponseWrapper(
                        SUCCESS,
                        "Login success",
                        userDataResponse), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                ERROR,
                "Incorrect Password",
                null), HttpStatus.OK);
    }

    @PutMapping(path = "/resetPassword")
    public ResponseEntity<ResponseWrapper> resetUserPassword(@RequestBody UserDataRequest resetRequest) {
        try {
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Password updated successfully",
                    userDataService.resetPassword(resetRequest)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Unable to find details for userId: " + resetRequest.getUserId(),
                    null), HttpStatus.OK);
        }
    }

    @GetMapping(path = "/userId/{userId}")
    public ResponseEntity<ResponseWrapper> getUserDetailsByUserId(@RequestBody UserDataRequest userRequest) {
        UUID userId;
        try {
             userId = UUID.fromString(userRequest.getUserId());
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Invalid UserId " + userRequest.getUserId(),
                    null),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "User Details fetched for userId: " + userRequest.getUserId(),
                userDataService.getUserDetailsById(userId)),
                HttpStatus.OK);
    }

    @PostMapping(path = "/sendResetEmail")
    public ResponseEntity<ResponseWrapper> sendResetEmail(@RequestBody UserDataRequest userRequest) {
        return sendEmail(userRequest.getEmailId(), EmailService.EmailType.RESET);
    }

    @PostMapping(path = "/sendWelcomeEmail")
    public ResponseEntity<ResponseWrapper> sendWelcomeEmail(@RequestBody UserDataRequest userRequest) {
        return sendEmail(userRequest.getEmailId(), EmailService.EmailType.WELCOME);
    }

    private ResponseEntity<ResponseWrapper> sendEmail(String emailId, EmailService.EmailType emailType) {
        try {
            emailService.sendEmail(emailId, emailType);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Email sent successfully",
                    null), HttpStatus.OK);
        } catch(MessagingException e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "User not registered with email " + emailId,
                    null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    e.getMessage(),
                    null), HttpStatus.OK);
        }
    }
}