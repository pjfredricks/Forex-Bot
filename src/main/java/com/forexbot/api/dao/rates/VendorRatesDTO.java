package com.forexbot.api.dao.rates;

import java.util.List;

public class VendorRatesDTO {

    private String vendorAgentId;
    private String vendorName;
    private List<ForexRequest> forexRequests;

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

    public List<ForexRequest> getForexRequests() {
        return forexRequests;
    }

    public void setForexRequests(List<ForexRequest> forexRequests) {
        this.forexRequests = forexRequests;
    }
}
