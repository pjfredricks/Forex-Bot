package com.example.demo.repository.dao;

import java.math.BigDecimal;

public class ForexRates {

    private String countryCode;
    private String countryName;
    private BigDecimal buyRate;
    private BigDecimal sellRate;

    public ForexRates() {
    }

    public ForexRates(String countryCode, String countryName, BigDecimal buyRate, BigDecimal sellRate) {
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
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
