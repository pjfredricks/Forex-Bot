package com.example.demo.service;

import java.io.IOException;

public interface SmsService {

    void sendOtp(String mobileNum, String otp) throws IOException;

    void sendConfirmation(String trackingNumber, String mobileNum) throws IOException;
}
