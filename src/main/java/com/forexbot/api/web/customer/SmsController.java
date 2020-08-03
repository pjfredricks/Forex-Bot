package com.forexbot.api.web.customer;

import com.forexbot.api.dao.otp.OtpRequest;
import com.forexbot.api.service.SmsService;
import com.forexbot.api.service.CustomerService;
import com.forexbot.api.web.utils.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.forexbot.api.web.utils.Constants.ERROR;
import static com.forexbot.api.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("api/v1")
public class SmsController {

    private final SmsService smsService;
    private final CustomerService customerService;

    public SmsController(SmsService smsService, CustomerService customerService) {
        this.smsService = smsService;
        this.customerService = customerService;
    }

    @PostMapping(path = "/sendOtp")
    public ResponseEntity<ResponseWrapper> sendOtp(@RequestBody OtpRequest otpRequest) {
        try {
            smsService.sendOtp(otpRequest.getMobileNum(), customerService.generateAndSaveOtp(otpRequest));
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
            if (customerService.verifyOtp(otpRequest)) {
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