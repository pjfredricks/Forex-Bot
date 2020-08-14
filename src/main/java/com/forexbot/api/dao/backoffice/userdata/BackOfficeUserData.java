package com.forexbot.api.dao.backoffice.userdata;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.forexbot.api.dao.backoffice.UserCategory;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

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
    @Column(name = "isActive", columnDefinition = "boolean default true")
    private boolean isActive;

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

    @Column(name = "vendorId", unique = true)
    private String vendorId;

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

    public BackOfficeUserData() {
        super();
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getCreatedBy() {
        return createdBy;
    }

    public void setCreatedBy(String createdBy) {
        this.createdBy = createdBy;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public boolean isActive() {
        return isActive;
    }

    public void setActive(boolean active) {
        isActive = active;
    }

    public UserCategory getUserCategory() {
        return userCategory;
    }

    public void setUserCategory(UserCategory userCategory) {
        this.userCategory = userCategory;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public String getEmailId() {
        return emailId;
    }

    public void setEmailId(String emailId) {
        this.emailId = emailId;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public String getAddressId() {
        return addressId;
    }

    public void setAddressId(String addressId) {
        this.addressId = addressId;
    }

    public String getVendorAgentId() {
        return vendorId;
    }

    public void setVendorAgentId(String vendorAgentId) {
        this.vendorId = vendorAgentId;
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

    public String getModifiedBy() {
        return modifiedBy;
    }

    public void setModifiedBy(String modifiedBy) {
        this.modifiedBy = modifiedBy;
    }

    public String getDeletedBy() {
        return deletedBy;
    }

    public void setDeletedBy(String deletedBy) {
        this.deletedBy = deletedBy;
    }

    public String getHexData() {
        return hexData;
    }

    public void setHexData(String hexData) {
        this.hexData = hexData;
    }
}
