package com.forexbot.api.web.customer;

import com.forexbot.api.dao.otp.OtpRequest;
import com.forexbot.api.service.CustomerService;
import com.forexbot.api.service.SmsService;
import com.forexbot.api.web.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.io.IOException;

import static com.forexbot.api.web.utils.ResponseWrapper.*;

@Slf4j
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class SmsController {

    private final SmsService smsService;
    private final CustomerService customerService;

    @PostMapping(path = "/sendOtp")
    public ResponseEntity<ResponseWrapper> sendOtp(@RequestBody OtpRequest otpRequest) {
        try {
            smsService.sendOtp(otpRequest.getMobileNum(), customerService.generateAndSaveOtp(otpRequest));
        } catch (IOException | IllegalAccessException e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(
                    buildSuccessResponse(e.getMessage(), null)
            );
        }

        return ResponseEntity.ok(
                buildSuccessResponse("Otp sent to number: " + otpRequest.getMobileNum(), null)
        );
    }

    @PostMapping(path = "/verifyOtp")
    public ResponseEntity<ResponseWrapper> verifyOtp(@RequestBody OtpRequest otpRequest) {
        try {
            if (customerService.verifyOtp(otpRequest)) {
                return ResponseEntity.ok(
                        buildSuccessResponse("Otp verification successful", null)
                );
            }
        } catch (IllegalAccessException e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(
                    buildErrorResponse(e.getMessage(), null)
            );
        }
        return ResponseEntity.ok(
                buildErrorResponse("Invalid OTP", null)
        );
    }

    @PostMapping(path = "/sendConfirmationSms")
    public ResponseEntity<ResponseWrapper> sendConfirmationSms(@RequestBody OtpRequest otpRequest) {
        try {
            smsService.sendConfirmation(otpRequest.getTrackingId(), otpRequest.getMobileNum());
        } catch (IOException e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(
                    buildSuccessResponse(e.getMessage(), null)
            );
        }

        return ResponseEntity.ok(
                buildSuccessResponse("Otp sent to number: " + otpRequest.getMobileNum(), null)
        );
    }
}
