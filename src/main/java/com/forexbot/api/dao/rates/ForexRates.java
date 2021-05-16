package com.forexbot.api.dao.rates;

import lombok.Data;

@Data
public class ForexRates {
    private boolean isCarousel;
    private String countryCode;
    private String countryName;
    private double buyRate;
    private double sellRate;
    private double latitude;
    private double longitude;
    private String currencyName;
}
