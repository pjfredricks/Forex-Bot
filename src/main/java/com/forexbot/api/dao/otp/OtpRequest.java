package com.forexbot.api.dao.otp;

public class OtpRequest {

    private String customerId;
    private String otp;
    private String mobileNum;
    private String emailId;
    private String trackingId;
    private int otpType;

    public OtpRequest() {
        super();
    }

    public String getCustomerId() {
        return customerId;
    }

    public void setCustomerId(String customerId) {
        this.customerId = customerId;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public int getOtpType() {
        return otpType;
    }

    public void setOtpType(int otpType) {
        this.otpType = otpType;
    }

    public String getTrackingId() {
        return trackingId;
    }

    public void setTrackingId(String trackingId) {
        this.trackingId = trackingId;
    }
}
