package com.example.demo.repository.dao.userdata;

public class OtpRequest {

    private String userId;
    private String otp;
    private String mobileNum;
    private int otpType;

    public OtpRequest() {
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
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

    public int getOtpType() {
        return otpType;
    }

    public void setOtpType(int otpType) {
        this.otpType = otpType;
    }
}
