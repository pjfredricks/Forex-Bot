package com.forexbot.api.dao.customer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;
import java.util.UUID;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "customerDetails")
@Entity
public class CustomerData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private int id;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "customerId", unique = true)
    private UUID customerId;

    @NotNull
    @Column(name = "name")
    private String name;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "emailId")
    private String emailId;

    @NotNull
    @Column(name = "isEmailVerified")
    private boolean isEmailVerified = false;

    @NotNull
    @Column(name = "mobileNum")
    private String mobileNum;

    @NotNull
    @Column(name = "isMobileVerified")
    private boolean isMobileVerified = false;

    @NotNull
    @Column(name = "isActive")
    private boolean isActive = true;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "createDate")
    private String createDate;

    @ApiModelProperty(hidden = true)
    @Column(name = "modifiedBy")
    private String modifiedBy;

    @ApiModelProperty(hidden = true)
    @Column(name = "modifiedDate")
    private String modifiedDate;

    @ApiModelProperty(hidden = true)
    @Column(name = "deletedBy")
    private String deletedBy;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "hexData")
    private String hexData;
}
