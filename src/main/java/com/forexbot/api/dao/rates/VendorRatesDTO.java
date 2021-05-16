package com.forexbot.api.dao.rates;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import io.swagger.annotations.ApiModelProperty;

import java.util.List;

@JsonIgnoreProperties(ignoreUnknown = true)
public class VendorRatesDTO {

    private String vendorAgentId;
    private String vendorName;
    private List<ForexData> forexData;
    @ApiModelProperty(hidden = true)
    private String createDate;
    @ApiModelProperty(hidden = true)
    private String modifiedDate;
    @ApiModelProperty(hidden = true)
    private boolean locked;

    public VendorRatesDTO() {
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

    public List<ForexData> getForexRequests() {
        return forexData;
    }

    public void setForexRequests(List<ForexData> forexData) {
        this.forexData = forexData;
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
