package com.forexbot.api.dao.otp;

import lombok.Data;

@Data
public class OtpRequest {
    private String customerId;
    private String otp;
    private String mobileNum;
    private String emailId;
    private String trackingId;
    private int otpType;
}
