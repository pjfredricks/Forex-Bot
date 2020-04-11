package com.example.demo.repository.dao;

public class ForexRates {

    private Boolean isCarousel;
    private String countryCode;
    private String countryName;
    private Double buyRate;
    private Double sellRate;

    public ForexRates() {
    }

    public ForexRates(Boolean isCarousel, String countryCode, String countryName, Double buyRate, Double sellRate) {
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

    public Double getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(Double buyRate) {
        this.buyRate = buyRate;
    }

    public Double getSellRate() {
        return sellRate;
    }

    public void setSellRate(Double sellRate) {
        this.sellRate = sellRate;
    }
}
