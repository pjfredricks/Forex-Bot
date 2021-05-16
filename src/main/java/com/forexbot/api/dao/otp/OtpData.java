package com.forexbot.api.dao.otp;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
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
    private boolean otpVerified = false;

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
}
