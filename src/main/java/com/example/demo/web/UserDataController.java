package com.example.demo.web;

import com.example.demo.repository.dao.ResponseWrapper;
import com.example.demo.repository.dao.userdata.*;
import com.example.demo.service.UserDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.example.demo.web.utils.Constants.ERROR;
import static com.example.demo.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("/api/v1")
public class UserDataController {

    private UserDataService userDataService;
    private EmailController emailController;

    public UserDataController(UserDataService userDataService, EmailController emailController) {
        this.userDataService = userDataService;
        this.emailController = emailController;
    }

    @PostMapping(path = "/signUp")
    public ResponseEntity<ResponseWrapper> signUpUser(@RequestBody UserDataRequest userDataRequest) {
        try {
            UserDataResponse response = userDataService.signUpUser(userDataRequest);
            emailController.sendWelcomeEmail(userDataRequest);
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

    @PostMapping(path = "/login")
    public ResponseEntity<ResponseWrapper> login(@RequestBody UserDataRequest userDataRequest) {
        UserDataResponse userDataResponse = null;
        try {
            userDataResponse = userDataService.login(userDataRequest);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "User not found",
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
            UserDataResponse response = userDataService.resetPassword(resetRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Password updated successfully",
                    response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Unable to find details for userId: " + resetRequest.getUserId(),
                    null), HttpStatus.OK);
        }
    }

    @PutMapping(path = "/updateDetails")
    public ResponseEntity<ResponseWrapper> updateUserDetails(@RequestBody UserDataRequest resetRequest) {
        try {
            UserDataResponse response = userDataService.updateUserDetails(resetRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "User details updated successfully",
                    response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    e.getMessage(),
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
}