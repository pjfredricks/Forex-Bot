package com.example.demo.web;

import com.example.demo.repository.dao.ResponseWrapper;
import com.example.demo.repository.dao.userdata.UserDataRequest;
import com.example.demo.repository.dao.userdata.UserDataResponse;
import com.example.demo.service.UserDataService;
import com.example.demo.web.utils.EmailUtil;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import javax.naming.NamingException;
import java.util.UUID;

@RestController
@RequestMapping("/api/v1")
public class UserDataController {

    private UserDataService userDataService;

    public UserDataController(UserDataService userDataService) {
        this.userDataService = userDataService;
    }

    @PostMapping(path = "/signUp")
    public ResponseEntity<ResponseWrapper> signUpUser(@RequestBody UserDataRequest userDataRequest) throws NamingException {
        if (EmailUtil.doLookup(userDataRequest.getEmailId())) {
            try {
                return new ResponseEntity<>(new ResponseWrapper(
                        "SUCCESS",
                        "userdata signed Up successfully",
                        userDataService.signUpUser(userDataRequest)), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseWrapper(
                        "ERROR",
                        "userdata details already exist for " + userDataRequest.getMobileNum() + " and " + userDataRequest.getEmailId(),
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

    @PutMapping(path = "/forgotPassword")
    public ResponseEntity<ResponseWrapper> updateUserPassword(@RequestBody UserDataRequest userDataRequest) {
        try {
            return new ResponseEntity<>(new ResponseWrapper(
                    "SUCCESS",
                    "password updated successfully",
                    userDataService.updatePassword(userDataRequest)), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    "ERROR",
                    "unable to find details for " + userDataRequest.getMobileNum() + " and " + userDataRequest.getEmailId(),
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
}