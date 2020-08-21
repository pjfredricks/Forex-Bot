package com.forexbot.api.dao.rates;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "vendorRatesData")
@Entity
public class VendorRatesData {

    @Id
    @GeneratedValue(strategy = GenerationType.AUTO)
    @ApiModelProperty(hidden = true)
    @JsonIgnore
    private int id;

    @NotNull
    @Column(name = "vendorAgentId", unique = true)
    private String vendorAgentId;

    @NotNull
    @Column(name = "vendorName")
    private String vendorName;

    @NotNull
    @Column(name = "ratesData", columnDefinition = "text", length = 25000)
    private String ratesData;

    @NotNull
    @Column(name = "createDate")
    private String createDate;

    @Column(name = "modifiedDate")
    private String modifiedDate;

    @NotNull
    @Column(name = "isLocked")
    private boolean locked;

    public VendorRatesData() {
        super();
        this.locked = false;
    }

    public String getVendorAgentId() {
        return vendorAgentId;
    }

    public void setVendorAgentId(String vendorAgentId) {
        this.vendorAgentId = vendorAgentId;
    }

    public String getVendorName() {
        return vendorName;
    }

    public void setVendorName(String vendorName) {
        this.vendorName = vendorName;
    }

    public String getRatesData() {
        return ratesData;
    }

    public void setRatesData(String ratesData) {
        this.ratesData = ratesData;
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

    public boolean isLocked() {
        return locked;
    }

    public void setLocked(boolean locked) {
        this.locked = locked;
    }
}
