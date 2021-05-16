package com.forexbot.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forexbot.api.dao.rates.ForexData;
import com.forexbot.api.dao.rates.ForexRates;
import com.forexbot.api.dao.rates.RatesData;
import com.forexbot.api.dao.rates.RatesStatus;
import com.forexbot.api.repository.RatesRepository;
import com.forexbot.api.service.RatesServiceV2;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;
import java.util.stream.Stream;

import static com.forexbot.api.web.utils.Constants.ZONE;

@Service
public class RatesServiceV2Impl implements RatesServiceV2 {

    private static final Logger LOGGER = LoggerFactory.getLogger(RatesServiceV2Impl.class);
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
    private List<ForexRates> forexRates;

    private final RatesRepository ratesRepository;

    public RatesServiceV2Impl(RatesRepository ratesRepository,
                              ObjectMapper mapper) {
        this.ratesRepository = ratesRepository;
        this.mapper = mapper;
        this.forexRates = new ArrayList<>();
    }

    @PostConstruct
    public void onStartup() {
        try {
            forexRates = mapper.readValue(
                    Thread
                            .currentThread()
                            .getContextClassLoader()
                            .getResourceAsStream("countryData/Country_Details.json"),
                    mapper.getTypeFactory()
                            .constructCollectionType(List.class, ForexRates.class));
        } catch (IOException e) {
            LOGGER.error("Unable to read country details json");
            forexRates = new ArrayList<>();
        }
    }

    @Override
    @Transactional
    public void updateRates(List<ForexData> ratesRequest, String approvedBy) {
        updateForexRates(ratesRequest);
        saveFinalRates(ratesRequest, approvedBy);
    }

    @Override
    public List<ForexRates> getExchangeRates() {
        return new ArrayList<>();
    }

    @Override
    public void goLive() {

    }

    private void updateForexRates(List<ForexData> ratesRequest) {
        ratesRequest.forEach(updatedRates -> forexRates
                .forEach(forexRate -> {
                    if (forexRate.getCountryCode().equals(updatedRates.getCountryCode())) {
                        forexRate.setBuyRate(updatedRates.getBuyRate());
                        forexRate.setSellRate(updatedRates.getSellRate());
                    }
                }));
    }

    private void saveFinalRates(List<ForexData> ratesRequest, String approvedBy) {
        RatesData ratesData = new RatesData();
        ratesData.setCreateDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
        ratesData.setApprovedBy(approvedBy);
        ratesData.setStatus(RatesStatus.OPEN);

        try {
            ratesData.setRatesDetails(mapper.writeValueAsString(ratesRequest));
        } catch (JsonProcessingException e) {
            ratesData.setRatesDetails("");
        }

        ratesRepository.save(ratesData);
    }
}
