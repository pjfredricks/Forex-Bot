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
import java.io.IOException;
import java.net.URISyntaxException;
import java.util.UUID;

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
                        "SUCCESS",
                        "User signed Up successfully",
                        response), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseWrapper(
                        "ERROR",
                        "User details already exist for " + userDataRequest.getMobileNum() + " and " + userDataRequest.getEmailId(),
                        null), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseWrapper(
                "ERROR",
                "Invalid EmailId: " + userDataRequest.getEmailId(),
                null), HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<ResponseWrapper> login(@RequestBody UserDataRequest userDataRequest) {
        UserDataResponse userDataResponse = null;
        try {
            userDataResponse = userDataService.login(userDataRequest);
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userDataResponse != null) {
                return new ResponseEntity<>(new ResponseWrapper(
                        "SUCCESS",
                        "Login success",
                        userDataResponse), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                "ERROR",
                "Incorrect Password",
                null), HttpStatus.OK);
    }

    @PutMapping(path = "/resetPassword")
    public ResponseEntity<ResponseWrapper> resetUserPassword(@RequestBody UserDataRequest userDataRequest) {
        try {
            return new ResponseEntity<>(new ResponseWrapper(
                    "SUCCESS",
                    "Password updated successfully",
                    userDataService.resetPassword(userDataRequest)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    "ERROR",
                    "Unable to find details for " + userDataRequest.getMobileNum() + " and " + userDataRequest.getEmailId(),
                    null), HttpStatus.OK);
        }
    }

    @GetMapping(path = "/userId/{userId}")
    public ResponseEntity<ResponseWrapper> getUserDetailsByUserId(@RequestParam String id) {
        UUID userId;
        try {
             userId = UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    "ERROR",
                    "Invalid UserId " + id,
                    null),
                    HttpStatus.BAD_REQUEST);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                "SUCCESS",
                "User Details fetched for userId: " + id,
                userDataService.getUserDetailsById(userId)),
                HttpStatus.OK);
    }

    @GetMapping(path = "/sendEmail")
    public ResponseEntity<ResponseWrapper> sendEmail(@RequestParam String emailId, @RequestParam EmailService.EmailType emailType) throws IOException, URISyntaxException {
        try {
            emailService.sendEmail(emailId, emailType);
            return new ResponseEntity<>(new ResponseWrapper(
                    "SUCCESS",
                    "Email sent successfully",
                    null), HttpStatus.OK);
        } catch(MessagingException e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    "ERROR",
                    "User not registered with email " + emailId,
                    null), HttpStatus.OK);
        } catch (Exception e) {
            System.out.println(System.getenv("spring.mail.password"));
            throw e;
//            return new ResponseEntity<>(new ResponseWrapper(
//                    "ERROR",
//                    e.getStackTrace().toString(),
//                    null), HttpStatus.OK);
        }
    }
}