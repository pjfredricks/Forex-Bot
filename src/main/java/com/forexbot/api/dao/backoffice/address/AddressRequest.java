package com.forexbot.api.dao.backoffice.address;

import lombok.Data;

@Data
public class AddressRequest {
    private String addressLine;
    private String city;
    private String state;
    private String pincode;
}
