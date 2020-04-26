package com.example.demo.repository.dao.rates;

public class ForexRates {

    private boolean isCarousel;
    private String countryCode;
    private String countryName;
    private double buyRate;
    private double sellRate;

    public ForexRates() {
        super();
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
