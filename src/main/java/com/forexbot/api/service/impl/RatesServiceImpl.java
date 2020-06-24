package com.forexbot.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forexbot.api.dao.order.OrderType;
import com.forexbot.api.dao.rates.DailyRates;
import com.forexbot.api.dao.rates.DailyRatesData;
import com.forexbot.api.dao.rates.ForexRates;
import com.forexbot.api.dao.rates.ForexRequest;
import com.forexbot.api.repository.RatesRepository;
import com.forexbot.api.service.RatesService;
import org.apache.commons.lang3.ObjectUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.BeanUtils;
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

    private static final ObjectMapper mapper = new ObjectMapper();
    private static Map<String, Number> currencyValues = new HashMap<>();
    private List<ForexRates> exchangeRates = new ArrayList<>();
    private List<DailyRates> dailyRates = new ArrayList<>();

    private final RatesRepository ratesRepository;

    public RatesServiceImpl(RatesRepository ratesRepository) {
        this.ratesRepository = ratesRepository;
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
    public void updateRates(String triggerIdentity) {
        // Removes old values from both collections
        currencyValues.clear();
        dailyRates.clear();

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

        updateDailyRates();
        saveDailyRates(triggerIdentity);
    }

    @Override
    public void updateRates(List<ForexRequest> ratesRequest, String triggerIdentity) {
        ratesRequest.forEach(updatedRates -> exchangeRates
                .forEach(forexRates -> {
            if (forexRates.getCountryCode().equals(updatedRates.getCountryCode())) {
                forexRates.setBuyRate(updatedRates.getBuyRate());
                forexRates.setSellRate(updatedRates.getSellRate());
            }
        }));
        updateDailyRates();
        saveDailyRates(triggerIdentity);
    }

    @Override
    public List<ForexRates> getExchangeRates() {
        if (exchangeRates.isEmpty() || dailyRates.isEmpty()) {
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

    private void updateDailyRates() {
        exchangeRates.forEach(exchangeRate -> {
            DailyRates response = new DailyRates();
            BeanUtils.copyProperties(exchangeRate, response);
            dailyRates.add(response);
        });
    }

    private void saveDailyRates(String triggerIdentity) {
        DailyRatesData ratesData = new DailyRatesData();

        ratesData.setTriggeredBy(triggerIdentity);
        ratesData.setCreateDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());

        try {
            ratesData.setRatesDetails(mapper.writeValueAsString(dailyRates));
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
                    .getForEntity("http://data.fixer.io/api/latest?access_key=4c29457e83b0090604057f85b8e874e3&format=1", LinkedHashMap.class)
                    .getBody()
                    .get("rates");
        } catch (Exception e) {
            LOGGER.error("Error fetching data from exchange rate api @ Time - {}", dateTimeFormatter.format(LocalDateTime.now(ZoneId.of(ZONE))));
        }
    }
}