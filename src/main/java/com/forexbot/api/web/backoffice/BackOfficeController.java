package com.forexbot.api.web.backoffice;

import com.forexbot.api.dao.backoffice.userdata.*;
import com.forexbot.api.service.BackOfficeService;
import com.forexbot.api.web.utils.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.forexbot.api.web.utils.Constants.ERROR;
import static com.forexbot.api.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("api/v1")
public class BackOfficeController {

    private final BackOfficeService backOfficeService;

    public BackOfficeController(BackOfficeService backOfficeService) {
        this.backOfficeService = backOfficeService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<ResponseWrapper> loginVendor(@RequestBody BackOfficeLoginRequest loginRequest) {
        try {
            BackOfficeLoginResponse response = backOfficeService.login(loginRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Login success",
                    response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Incorrect Credentials, Error:" + e.getMessage(),
                    null), HttpStatus.OK);
        }
    }

    @PostMapping(path = "/create/admin")
    public ResponseEntity<ResponseWrapper> createAdmin(@RequestBody BackOfficeSignInRequest signInRequest) {
        try {
            BackOfficeUserData responseData = backOfficeService.createAdmin(signInRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Admin created successfully for user: " + responseData.getUserName(),
                    responseData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Admin already registered, Error: " + e.getMessage(),
                    null), HttpStatus.OK);
        }
    }

    @PostMapping(path = "/create/vendor")
    public ResponseEntity<ResponseWrapper> createVendor(@RequestBody BackOfficeSignInRequest signInRequest) {

        try {
            BackOfficeUserData responseData = backOfficeService.createVendor(signInRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Vendor registered successfully for id: " + responseData.getVendorAgentId(),
                    responseData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Vendor already registered, Error:" + e.getMessage(),
                    null), HttpStatus.OK);
        }
    }

    @PutMapping(path = "/update/{userId}")
    public ResponseEntity<ResponseWrapper> update(@RequestBody BackOfficeSignInRequest signInRequest,
                                                  @PathVariable String userId) {
        try {
            BackOfficeUserResponse responseData = backOfficeService.update(signInRequest, userId);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Details updated successfully for user: " + responseData.getUserName(),
                    responseData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Error updating details, Error:" + e.getMessage(),
                    null), HttpStatus.OK);
        }
    }
}
