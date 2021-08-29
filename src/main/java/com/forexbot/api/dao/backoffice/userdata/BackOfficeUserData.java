package com.forexbot.api.dao.backoffice.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forexbot.api.dao.backoffice.UserCategory;
import io.swagger.annotations.ApiModelProperty;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Getter
@Setter
@NoArgsConstructor
@Table(name = "backOfficeUserData")
@Entity
public class BackOfficeUserData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private int id;

    @NotNull
    @Column(name = "userId", unique = true)
    private String userId;

    @Column(name = "createdBy")
    private String createdBy;

    @NotNull
    @Column(name = "userName")
    private String userName;

    @NotNull
    @Column(name = "active")
    private boolean active = true;

    @NotNull
    @Column(name = "userCategory")
    @Enumerated(EnumType.STRING)
    private UserCategory userCategory;

    @NotNull
    @Column(name = "password")
    private String password;

    @NotNull
    @Column(name = "emailId")
    private String emailId;

    @NotNull
    @Column(name = "mobileNum")
    private String mobileNum;

    @NotNull
    @Column(name = "addressId", unique = true)
    private String addressId;

    @Column(name = "vendorAgentId", unique = true)
    private String vendorAgentId;

    @NotNull
    @ApiModelProperty(hidden = true)
    @Column(name = "createDate")
    private String createDate;

    @ApiModelProperty(hidden = true)
    @Column(name = "modifiedDate")
    private String modifiedDate;

    @ApiModelProperty(hidden = true)
    @Column(name = "modifiedBy")
    private String modifiedBy;

    @ApiModelProperty(hidden = true)
    @Column(name = "deletedBy")
    private String deletedBy;

    @ApiModelProperty(hidden = true)
    @Column(name = "hexData")
    private String hexData;
}
