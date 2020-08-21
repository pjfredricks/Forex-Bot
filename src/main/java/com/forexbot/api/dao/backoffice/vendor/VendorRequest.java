package com.forexbot.api.dao.backoffice.vendor;

public class VendorRequest {

    private String vendorName;
    private String gstNumber;
    private String rbiagentId;
    private String panNumber;

    public VendorRequest() {
        super();
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
