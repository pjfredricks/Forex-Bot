package com.forexbot.api.service;

import java.io.IOException;

public interface SmsService {

    void sendOtp(String mobileNum, String otp) throws IOException;

    void sendConfirmation(String trackingNumber, String mobileNum) throws IOException;
}
