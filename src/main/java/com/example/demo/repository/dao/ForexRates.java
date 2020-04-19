package com.example.demo.repository.dao;

public class ForexRates {

    private boolean isCarousel;
    private String countryCode;
    private String countryName;
    private double buyRate;
    private double sellRate;

    public ForexRates() {
    }

    public ForexRates(boolean isCarousel, String countryCode, String countryName, double buyRate, double sellRate) {
        this.isCarousel = isCarousel;
        this.countryCode = countryCode;
        this.countryName = countryName;
        this.buyRate = buyRate;
        this.sellRate = sellRate;
    }

    public boolean isCarousel() {
        return isCarousel;
    }

    public void setCarousel(boolean carousel) {
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

    public double getBuyRate() {
        return buyRate;
    }

    public void setBuyRate(double buyRate) {
        this.buyRate = buyRate;
    }

    public double getSellRate() {
        return sellRate;
    }

    public void setSellRate(double sellRate) {
        this.sellRate = sellRate;
    }
}
