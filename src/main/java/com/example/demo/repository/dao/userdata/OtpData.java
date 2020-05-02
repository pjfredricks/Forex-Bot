package com.example.demo.repository.dao.userdata;

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
    @Column(name = "otpVerified")
    private boolean otpVerified;

    @NotNull
    @Column(name = "emailId", unique = true)
    private String emailId;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "createDate")
    private String createDate;

    @ApiModelProperty(hidden = true)
    @Column(name = "modifiedDate")
    private String modifiedDate;

    public OtpData() {
        super();
        otpVerified = false;
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

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
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
}
