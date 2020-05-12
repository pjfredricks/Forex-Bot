package com.example.demo.repository.dao.otp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Table(name = "otpData")
@Entity
public class OtpData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private int id;

    @ApiModelProperty(hidden = true)
    @Column(name = "userId")
    private UUID userId;

    @NotNull
    @Column(name = "otp")
    private String otp;

    @NotNull
    @Column(name = "otpType")
    private OtpType otpType;

    @NotNull
    @Column(name = "retryCount")
    private int retryCount;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "otpVerified", columnDefinition = "boolean default false")
    private boolean otpVerified;

    @NotNull
    @Column(name = "mobileNumber", unique = true)
    private String mobileNumber;

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
    }

    public UUID getUserId() {
        return userId;
    }

    public void setUserId(UUID userId) {
        this.userId = userId;
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

    public String getMobileNumber() {
        return mobileNumber;
    }

    public void setMobileNumber(String mobileNumber) {
        this.mobileNumber = mobileNumber;
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
