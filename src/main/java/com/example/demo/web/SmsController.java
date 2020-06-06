package com.example.demo.web;

import com.example.demo.repository.dao.ResponseWrapper;
import com.example.demo.repository.dao.otp.OtpRequest;
import com.example.demo.service.SmsService;
import com.example.demo.service.UserDataService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.example.demo.web.utils.Constants.ERROR;
import static com.example.demo.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("api/v1")
public class SmsController {

    private final SmsService smsService;
    private final UserDataService userDataService;

    public SmsController(SmsService smsService, UserDataService userDataService) {
        this.smsService = smsService;
        this.userDataService = userDataService;
    }

    @PostMapping(path = "/sendOtp")
    public ResponseEntity<ResponseWrapper> sendOtp(@RequestBody OtpRequest otpRequest) {
        try {
            smsService.sendOtp(otpRequest.getMobileNum(), userDataService.generateAndSaveOtp(otpRequest));
        } catch (IOException | IllegalAccessException e) {
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

    @PostMapping(path = "/sendConfirmationSms")
    public ResponseEntity<ResponseWrapper> sendConfirmationSms(@RequestBody OtpRequest otpRequest) {
        try {
            smsService.sendConfirmation(otpRequest.getTrackingId(), otpRequest.getMobileNum());
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
}