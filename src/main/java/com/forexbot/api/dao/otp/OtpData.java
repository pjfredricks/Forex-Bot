package com.forexbot.api.dao.otp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "otpData")
@Entity
public class OtpData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private int id;

    @NotNull
    @Column(name = "otp")
    private String otp;

    @NotNull
    @Column(name = "otpType")
    @Enumerated(EnumType.STRING)
    private OtpType otpType;

    @NotNull
    @Column(name = "retryCount")
    private int retryCount;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "otpVerified")
    private boolean otpVerified;

    @NotNull
    @Column(name = "mobileNum", unique = true)
    private String mobileNum;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "createDate")
    private String createDate;

    @ApiModelProperty(hidden = true)
    @Column(name = "modifiedDate")
    private String modifiedDate;

    @Column(name = "textLocalResponse", columnDefinition = "text default 'empty'")
    private String textLocalResponse;

    public OtpData() {
        super();
        this.otpVerified = false;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public boolean isOtpVerified() {
        return otpVerified;
    }

    public void setOtpVerified(boolean otpVerified) {
        this.otpVerified = otpVerified;
    }

    public OtpType getOtpType() {
        return otpType;
    }

    public void setOtpType(OtpType otpType) {
        this.otpType = otpType;
    }

    public int getRetryCount() {
        return retryCount;
    }

    public void setRetryCount(int retryCount) {
        this.retryCount = retryCount;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getCreateDate() {
        return createDate;
    }

    public void setCreateDate(String createDate) {
        this.createDate = createDate;
    }

    public String getModifiedDate() {
        return modifiedDate;
    }

    public void setModifiedDate(String modifiedDate) {
        this.modifiedDate = modifiedDate;
    }

    public String getTextLocalResponse() {
        return textLocalResponse;
    }

    public void setTextLocalResponse(String textLocalResponse) {
        this.textLocalResponse = textLocalResponse;
    }
}
