package com.example.demo.service.impl;

import com.example.demo.repository.dao.ForexRates;
import com.example.demo.repository.dao.Order.OrderType;
import com.example.demo.service.RatesService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

@Service
public class RatesServiceImpl implements RatesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatesServiceImpl.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final Set<String> countryList = Stream.of("USD", "AUD", "GBP", "EUR", "CAD",
            "CNY", "SAR", "SGD", "MYR", "THB",
            "IDR", "ILS", "JPY", "KRW", "CHF",
            "PHP", "FJD", "HKD", "ZAR").collect(Collectors.toSet());
    private static final Set<String> noCarouselCountryList = Stream.of("ILS", "JPY", "KRW", "CHF",
            "PHP", "FJD", "HKD", "ZAR").collect(Collectors.toSet());

    private static Map<String, Double> currencyValues = new HashMap<>();
    private static List<ForexRates> exchangeRates = new ArrayList<>();

    @PostConstruct
    public void onStartup() {
        updateExchangeRates();
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void scheduledTask() {
        LOGGER.info("Updating Database with latest Rates @ Time - {}", dateTimeFormatter.format(LocalDateTime.now(ZoneId.of("Asia/Kolkata"))));
        updateExchangeRates();
    }

    @Override
    public void updateExchangeRates() {
        // Removes old values from both collections
        exchangeRates.clear();
        currencyValues.clear();

        // Updates latest forex currency values
        getCurrencyForexValues();

        // Remove unwanted countries and convert rates
        currencyValues.keySet().retainAll(countryList);
        currencyValues.replaceAll((countryCode, currencyValue) -> convertRate(currencyValue));

        // Set buy and sell Rates, and update carousel values
        exchangeRates = currencyValues.entrySet()
                .stream()
                .map(currencyValueMap -> constructForexRates(currencyValueMap))
                .sorted(Comparator.comparing(ForexRates::getCountryName))
                .collect(Collectors.toList());
    }

    @Override
    public List<ForexRates> getExchangeRates() {
        if (exchangeRates.isEmpty()) {
            updateExchangeRates();
        }
        return exchangeRates;
    }

    @Override
    public double getRateByCountryCodeAndType(String countryCode, OrderType orderType) {
        ForexRates rates = exchangeRates.stream()
                .filter(forexRates -> forexRates.getCountryCode()
                        .equals(countryCode))
                .findAny()
                .get();

        switch (orderType) {
            case SELL:
                return rates.getSellRate();
            case BUY:
                return rates.getBuyRate();
            default:
                return 0.00d;
        }
    }

    private ForexRates constructForexRates(Map.Entry<String, Double> currencyValueMap) {
        ForexRates forexRate = new ForexRates();

        if (noCarouselCountryList.contains(currencyValueMap.getKey())) {
            forexRate.setCarousel(false);
        } else {
            forexRate.setCarousel(true);
        }
        forexRate.setCountryCode(currencyValueMap.getKey());
        forexRate.setCountryName(Currency.getInstance(currencyValueMap.getKey()).getDisplayName());
        forexRate.setBuyRate(calculateBuyRate(currencyValueMap.getValue()));
        forexRate.setSellRate(calculateSellRate(currencyValueMap.getValue()));
        return forexRate;
    }

    private double calculateBuyRate(double currencyValue) {
        int percent = 2;

        if (currencyValue < 5.000) percent = 6;
        else
        if (currencyValue < 30.000) percent = 5;
        else
        if (currencyValue < 80.000) percent = 3;

        currencyValue =  currencyValue - (currencyValue/100) * percent;
        return BigDecimal.valueOf(currencyValue).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    private double calculateSellRate(double currencyValue) {
        int percent = 1;

        if (currencyValue < 5.000) percent = 4;
        else
        if (currencyValue < 30.000) percent = 3;
        else
        if (currencyValue < 80.000) percent = 2;

        currencyValue =  currencyValue - (currencyValue/100) * percent;
        return BigDecimal.valueOf(currencyValue).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    private double convertRate(double currencyValue) {
        return BigDecimal.valueOf(1 / currencyValue).setScale(2, RoundingMode.HALF_EVEN).doubleValue();
    }

    private static void getCurrencyForexValues() {
        try {
            currencyValues = (Map<String, Double>) new RestTemplate()
                    .getForEntity("https://api.exchangerate-api.com/v4/latest/INR", LinkedHashMap.class)
                    .getBody()
                    .get("rates");
        } catch (Exception e) {
            LOGGER.error("Error fetching data from exchange rate api @ Time - {}", dateTimeFormatter.format(LocalDateTime.now(ZoneId.of("Asia/Kolkata"))));
        }
    }
}