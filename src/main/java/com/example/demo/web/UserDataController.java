package com.example.demo.web;

import com.example.demo.repository.dao.ResponseWrapper;
import com.example.demo.repository.dao.UserData.UserDataRequest;
import com.example.demo.repository.dao.UserData.UserDataResponse;
import com.example.demo.service.UserDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
public class UserDataController {

    private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    private UserDataService userDataService;

    public UserDataController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @PostMapping(path = "/signUp")
    public ResponseEntity<ResponseWrapper> signUpUser(@RequestBody UserDataRequest userDataRequest) {
        if (userDataRequest.getEmailId().matches(EMAIL_REGEX)) {
            try {
                return new ResponseEntity<>(new ResponseWrapper(
                        "SUCCESS",
                        "UserData signed Up successfully",
                        userDataService.signUpUser(userDataRequest)), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseWrapper(
                        "ERROR",
                        "UserData details already exist for " + userDataRequest.getMobileNum() + " and " + userDataRequest.getEmailId(),
                        null), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseWrapper(
                "SUCCESS",
                "Invalid Email",
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
}