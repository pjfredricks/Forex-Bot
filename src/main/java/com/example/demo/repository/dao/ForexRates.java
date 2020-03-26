package com.example.demo.repository.dao;

import java.math.BigDecimal;

public class ForexRates {

    private Boolean isCarousel;
    private String countryCode;
    private String countryName;
    private BigDecimal buyRate;
    private BigDecimal sellRate;

    public ForexRates() {
    }

    public ForexRates(Boolean isCarousel, String countryCode, String countryName, BigDecimal buyRate, BigDecimal sellRate) {
        this.isCarousel = isCarousel;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
    }

    public Boolean isCarousel() {
        return isCarousel;
    }

    public void setCarousel(Boolean carousel) {
        isCarousel = carousel;
    }

    public String getCountryCode() {
        return countryCode;
    }

    public void setCountryCode(String countryCode) {
        this.countryCode = countryCode;
    }

    public String getCountryName() {
        return countryName;
    }

    public void setCountryName(String countryName) {
        this.countryName = countryName;
    }

    public BigDecimal getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(BigDecimal buyRate) {
        this.buyRate = buyRate;
    }

    public BigDecimal getSellRate() {
        return sellRate;
    }

    public void setSellRate(BigDecimal sellRate) {
        this.sellRate = sellRate;
    }
}
