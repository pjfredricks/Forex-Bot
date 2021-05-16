package com.forexbot.api.dao.rates;

import lombok.Data;

@Data
public class ForexData {
    private String countryCode;
    private double buyRate;
    private double sellRate;
}
