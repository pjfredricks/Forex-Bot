package com.forexbot.api.dao.backoffice.vendor;

import lombok.Data;

@Data
public class VendorRequest {
    private String vendorName;
    private String gstNumber;
    private String rbiagentId;
    private String panNumber;
}
