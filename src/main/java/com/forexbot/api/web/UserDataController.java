package com.forexbot.api.web;

import com.forexbot.api.dao.userdata.UserDataRequest;
import com.forexbot.api.dao.userdata.UserDataResponse;
import com.forexbot.api.service.UserDataService;
import com.forexbot.api.web.utils.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.forexbot.api.web.utils.Constants.ERROR;
import static com.forexbot.api.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("/api/v1")
public class UserDataController {

    private final UserDataService userDataService;
    private final EmailController emailController;

    public UserDataController(UserDataService userDataService, EmailController emailController) {
        this.userDataService = userDataService;
        this.emailController = emailController;
    }

    @GetMapping(path = "/users")
    public ResponseEntity<ResponseWrapper> getAllUsers() {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "All users details fetched",
                userDataService.getAllUsers()), HttpStatus.OK);
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
        UserDataResponse userDataResponse = userDataService.login(userDataRequest);
        if (userDataResponse != null) {
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Login success",
                    userDataResponse), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                ERROR,
                "Incorrect Credentials",
                null), HttpStatus.OK);
    }

    // Not integrated with UI
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
                    e.getMessage(),
                    null), HttpStatus.OK);
        }
    }

    // Not integrated with UI
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

    @DeleteMapping(path = "/user/{userId}")
    public ResponseEntity<ResponseWrapper> deleteUserById(@PathVariable String userId) {
        userDataService.deleteUser(UUID.fromString(userId));
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "User deleted successfully",
                null),
                HttpStatus.OK);
    }

    @GetMapping(path = "/user/{userId}")
    public ResponseEntity<ResponseWrapper> getUserDetailsByUserId(@PathVariable String userId) {
        try {
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "User Details fetched for userId: " + userId,
                    userDataService.getUserDetailsById(UUID.fromString(userId))),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Invalid UserId " + userId,
                    null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/user/mobileNum/{mobileNum}")
    public ResponseEntity<ResponseWrapper> getUserDetailsByMobileNum(@PathVariable String mobileNum) {
        try {
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "User Details fetched for mobile number: " + mobileNum,
                    userDataService.getUserDataByEmailIdOrMobileNum(null, mobileNum)),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Invalid mobile number " + mobileNum,
                    null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/user/emailId/{emailId}")
    public ResponseEntity<ResponseWrapper> getUserDetailsByEmailId(@PathVariable String emailId) {
        try {
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "User Details fetched for emailId: " + emailId,
                    userDataService.getUserDataByEmailIdOrMobileNum(emailId, null)),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Invalid emailId " + emailId,
                    null),
                    HttpStatus.BAD_REQUEST);
        }
    }
}