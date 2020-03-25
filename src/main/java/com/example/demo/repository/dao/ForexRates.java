package com.example.demo.repository.dao;

import java.math.BigDecimal;

public class ForexRates {

    private String country;
    private BigDecimal buyRate;
    private BigDecimal sellRate;

    public ForexRates() {
    }

    public ForexRates(String country, BigDecimal buyRate, BigDecimal sellRate) {
        this.country = country;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
    }

    public String getCountry() {
        return country;
    }

    public void setCountry(String country) {
        this.country = country;
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
