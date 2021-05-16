package com.forexbot.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forexbot.api.dao.order.OrderType;
import com.forexbot.api.dao.rates.RatesData;
import com.forexbot.api.dao.rates.ForexRates;
import com.forexbot.api.dao.rates.ForexData;
import com.forexbot.api.repository.RatesRepository;
import com.forexbot.api.service.RatesService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.forexbot.api.web.utils.Constants.ZONE;

@Service
@Deprecated
public class RatesServiceImpl implements RatesService {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatesServiceImpl.class);
    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");
    private static final Set<String> countryList = Stream.of("INR", "USD", "AUD", "GBP", "EUR", "CAD",
            "CNY", "SAR", "SGD", "MYR", "THB",
            "IDR", "ILS", "JPY", "KRW", "CHF",
            "PHP", "FJD", "HKD", "ZAR", "SEK",
            "NOK", "DKK", "NZD", "BHD", "OMR",
            "KWD").collect(Collectors.toSet());
    private static final Set<String> noCarouselCountryList = Stream.of("INR", "ILS", "JPY", "KRW", "CHF",
            "PHP", "FJD", "HKD", "ZAR",
            "SEK", "NOK", "DKK", "NZD",
            "BHD", "OMR", "KWD").collect(Collectors.toSet());

    private final ObjectMapper mapper;
    private static Map<String, Number> currencyValues = new HashMap<>();
    private List<ForexRates> exchangeRates = new ArrayList<>();

    private final RatesRepository ratesRepository;

    public RatesServiceImpl(RatesRepository ratesRepository,
                            ObjectMapper mapper) {
        this.ratesRepository = ratesRepository;
        this.mapper = mapper;
    }

    @PostConstruct
    public void onStartup() {
        try {
            exchangeRates = mapper.readValue(
                    Thread
                            .currentThread()
                            .getContextClassLoader()
                            .getResourceAsStream("countryData/Country_Details.json"),
                    mapper.getTypeFactory()
                            .constructCollectionType(List.class, ForexRates.class));
        } catch (IOException e) {
            exchangeRates = new ArrayList<>();
        }
        updateRates("Startup");
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void scheduledTask() {
        LOGGER.info("Updating Database with latest Rates @ Time - {}", dateTimeFormatter.format(LocalDateTime.now(ZoneId.of(ZONE))));
        updateRates("Cron Job");
    }

    @Override
    @Scheduled(cron = "0 0 11 * * *")
    public void deleteTempRates() {
        saveFinalRates("End of time");
    }

    @Override
    public void updateRates(String approvedBy) {
        // Removes old values from collection
        currencyValues.clear();

        // Updates latest forex currency values
        getCurrencyForexValues();
        double inrValue = currencyValues.get("INR").doubleValue();

        // Remove unwanted countries and convert rates
        currencyValues.keySet().retainAll(countryList);
        currencyValues.entrySet().forEach(currencyValue -> currencyValue.setValue(currencyValue.getValue().doubleValue() / inrValue));
        currencyValues.replaceAll((countryCode, currencyValue) -> convertRate(1 / currencyValue.doubleValue(), 6));

        // Set buy and sell Rates, and update carousel values
        exchangeRates = exchangeRates.stream()
                .map(forexRate -> setCarouselAndRates(forexRate, currencyValues))
                .sorted(Comparator.comparing(ForexRates::getCountryName))
                .collect(Collectors.toList());

        saveFinalRates(approvedBy);
    }

    @Override
    public void updateRates(List<ForexData> ratesRequest, String approvedBy) {
        ratesRequest.forEach(updatedRates -> exchangeRates
                .forEach(forexRates -> {
            if (forexRates.getCountryCode().equals(updatedRates.getCountryCode())) {
                forexRates.setBuyRate(updatedRates.getBuyRate());
                forexRates.setSellRate(updatedRates.getSellRate());
            }
        }));
        saveFinalRates(approvedBy);
    }

    @Override
    public List<ForexRates> getExchangeRates() {
        if (exchangeRates.isEmpty()) {
            updateRates("Api call");
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

    private void saveFinalRates(String approvedBy) {
        RatesData ratesData = new RatesData();
        ratesData.setApprovedBy(approvedBy);
        ratesData.setCreateDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());

        try {
            ratesData.setRatesDetails(mapper.writeValueAsString(exchangeRates));
        } catch (JsonProcessingException e) {
            ratesData.setRatesDetails("");
        }

        ratesRepository.save(ratesData);
    }

    private ForexRates setCarouselAndRates(ForexRates forexRate, Map<String, Number> currencyValues) {
        forexRate.setCarousel(true);

        if (noCarouselCountryList.contains(forexRate.getCountryCode())) {
            forexRate.setCarousel(false);
        }
        if(ObjectUtils.isNotEmpty(currencyValues.get(forexRate.getCountryCode()))) {
            forexRate.setBuyRate(
                    calculateBuyRate(currencyValues.get(forexRate.getCountryCode()).doubleValue()));
            forexRate.setSellRate(
                    calculateSellRate(currencyValues.get(forexRate.getCountryCode()).doubleValue()));
        }
        return forexRate;
    }

    private double calculateBuyRate(double currencyValue) {
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

    private double calculateSellRate(double currencyValue) {
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

    private double convertRate(double currencyValue, int scalingValue) {
        return BigDecimal.valueOf(currencyValue).setScale(scalingValue, RoundingMode.HALF_EVEN).doubleValue();
    }

    private void getCurrencyForexValues() {
        try {
            currencyValues = (Map<String, Number>) new RestTemplate()
                    .getForEntity("https://api.exchangerate-api.com/v4/latest/INR", LinkedHashMap.class)
                    .getBody()
                    .get("rates");
        } catch (Exception e) {
            LOGGER.error("Error fetching data from exchange rate api @ Time - {}", dateTimeFormatter.format(LocalDateTime.now(ZoneId.of(ZONE))));
        }
    }
}
