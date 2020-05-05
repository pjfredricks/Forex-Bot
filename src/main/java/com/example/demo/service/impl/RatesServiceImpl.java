package com.example.demo.service.impl;

import com.example.demo.repository.dao.rates.ForexRates;
import com.example.demo.repository.dao.order.OrderType;
import com.example.demo.repository.dao.rates.RatesRequest;
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

import static com.example.demo.web.utils.Constants.ZONE;

@Service
public class RatesServiceImpl implements RatesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatesServiceImpl.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final Set<String> countryList = Stream.of("USD", "AUD", "GBP", "EUR", "CAD",
            "CNY", "SAR", "SGD", "MYR", "THB",
            "IDR", "ILS", "JPY", "KRW", "CHF",
            "PHP", "FJD", "HKD", "ZAR", "SEK",
            "NOK", "DKK", "NZD", "BHD", "OMR",
            "KWD").collect(Collectors.toSet());
    private static final Set<String> noCarouselCountryList = Stream.of("ILS", "JPY", "KRW", "CHF",
            "PHP", "FJD", "HKD", "ZAR",
            "SEK", "NOK", "DKK", "NZD",
            "BHD", "OMR", "KWD").collect(Collectors.toSet());

    private static Map<String, Number> currencyValues = new HashMap<>();
    private static List<ForexRates> exchangeRates = new ArrayList<>();

    @PostConstruct
    public void onStartup() {
        updateExchangeRates();
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void scheduledTask() {
        LOGGER.info("Updating Database with latest Rates @ Time - {}", dateTimeFormatter.format(LocalDateTime.now(ZoneId.of(ZONE))));
        updateExchangeRates();
    }

    @Override
    public void updateExchangeRates() {
        // Removes old values from both collections
        exchangeRates.clear();
        currencyValues.clear();

        // Updates latest forex currency values
        getCurrencyForexValues();
        double inrValue = currencyValues.get("INR").doubleValue();

        // Remove unwanted countries and convert rates
        currencyValues.keySet().retainAll(countryList);
        currencyValues.entrySet().forEach(currencyValue -> currencyValue.setValue(currencyValue.getValue().doubleValue() / inrValue));
        currencyValues.replaceAll((countryCode, currencyValue) -> convertRate(1 / currencyValue.doubleValue(), 6));

        // Set buy and sell Rates, and update carousel values
        exchangeRates = currencyValues.entrySet()
                .stream()
                .map(RatesServiceImpl::constructForexRates)
                .sorted(Comparator.comparing(ForexRates::getCountryName))
                .collect(Collectors.toList());
    }

    @Override
    public void updateExchangeRates(List<RatesRequest> ratesRequest) {
        ratesRequest.forEach(updatedRates -> exchangeRates
                .forEach(forexRates -> {
            if (forexRates.getCountryCode().equals(updatedRates.getCountryCode())) {
                forexRates.setBuyRate(updatedRates.getBuyRate());
                forexRates.setSellRate(updatedRates.getSellRate());
            }
        }));
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
        Optional<ForexRates> rates = exchangeRates.stream()
                .filter(forexRates -> forexRates.getCountryCode().equals(countryCode))
                .findFirst();

        if (!rates.isPresent()) {
            return 0.00d;
        }

        switch (orderType) {
            case SELL:
                return rates.get().getSellRate();
            case BUY:
                return rates.get().getBuyRate();
            default:
                return 0.00d;
        }
    }

    private static ForexRates constructForexRates(Map.Entry<String, Number> currencyValueMap) {
        ForexRates forexRate = new ForexRates();
        forexRate.setCarousel(true);

        if (noCarouselCountryList.contains(currencyValueMap.getKey())) {
            forexRate.setCarousel(false);
        }
        forexRate.setCountryCode(currencyValueMap.getKey());
        forexRate.setCountryName(Currency.getInstance(currencyValueMap.getKey()).getDisplayName());
        forexRate.setBuyRate(calculateBuyRate(currencyValueMap.getValue().doubleValue()));
        forexRate.setSellRate(calculateSellRate(currencyValueMap.getValue().doubleValue()));
        return forexRate;
    }

    private static double calculateBuyRate(double currencyValue) {
        int percent = 2;

        if (currencyValue < 0.01) {
            return convertRate(currencyValue, 4);
        }
        if (currencyValue < 5.000) percent = 6;
        else
        if (currencyValue < 30.000) percent = 5;
        else
        if (currencyValue < 80.000) percent = 3;

        currencyValue =  currencyValue - (currencyValue/100) * percent;
        return convertRate(currencyValue, 2);
    }

    private static double calculateSellRate(double currencyValue) {
        int percent = 1;

        if (currencyValue < 0.01) {
            return convertRate(currencyValue, 4);
        }

        if (currencyValue < 5.000) percent = 4;
        else
        if (currencyValue < 30.000) percent = 3;
        else
        if (currencyValue < 80.000) percent = 2;

        currencyValue =  currencyValue - (currencyValue/100) * percent;
        return convertRate(currencyValue, 2);
    }

    private static double convertRate(double currencyValue, int scalingValue) {
        return BigDecimal.valueOf(currencyValue).setScale(scalingValue, RoundingMode.HALF_EVEN).doubleValue();
    }

    private static void getCurrencyForexValues() {
        try {
            currencyValues = (Map<String, Number>) new RestTemplate()
                    .getForEntity("http://data.fixer.io/api/latest?access_key=4c29457e83b0090604057f85b8e874e3&format=1", LinkedHashMap.class)
                    .getBody()
                    .get("rates");
        } catch (Exception e) {
            LOGGER.error("Error fetching data from exchange rate api @ Time - {}", dateTimeFormatter.format(LocalDateTime.now(ZoneId.of(ZONE))));
        }
    }
}