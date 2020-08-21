package com.forexbot.api.dao.backoffice.vendor;

import com.fasterxml.jackson.annotation.JsonIgnore;
import io.swagger.annotations.ApiModelProperty;

import javax.persistence.*;
import javax.validation.constraints.NotNull;

@Table(name = "vendorData")
@Entity
public class VendorData {

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
    @Column(name = "gstNumber")
    private String gstNumber;

    @NotNull
    @Column(name = "rbiagentId")
    private String rbiagentId;

    @NotNull
    @Column(name = "panNumber")
    private String panNumber;

    public VendorData() {
        super();
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

    public String getGstNumber() {
        return gstNumber;
    }

    public void setGstNumber(String gstNumber) {
        this.gstNumber = gstNumber;
    }

    public String getRbiagentId() {
        return rbiagentId;
    }

    public void setRbiagentId(String rbiagentId) {
        this.rbiagentId = rbiagentId;
    }

    public String getPanNumber() {
        return panNumber;
    }

    public void setPanNumber(String panNumber) {
        this.panNumber = panNumber;
    }
}
