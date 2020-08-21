package com.forexbot.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forexbot.api.dao.rates.ForexRates;
import com.forexbot.api.dao.rates.VendorRatesDTO;
import com.forexbot.api.dao.rates.VendorRatesData;
import com.forexbot.api.repository.VendorRatesRepository;
import com.forexbot.api.service.VendorRatesService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import javax.annotation.PostConstruct;
import java.io.IOException;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.ZoneId;
import java.time.temporal.ChronoUnit;
import java.util.ArrayList;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

import static com.forexbot.api.web.utils.Constants.ZONE;

@Service
public class VendorRatesServiceImpl implements VendorRatesService {

    private List<ForexRates> exchangeRates;
    private final ObjectMapper mapper;

    private final VendorRatesRepository vendorRatesRepository;

    public VendorRatesServiceImpl(VendorRatesRepository vendorRatesRepository) {
        this.vendorRatesRepository = vendorRatesRepository;
        mapper = new ObjectMapper();
        exchangeRates = new ArrayList<>();
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
    }

    @Override
    public List<ForexRates> getCountries() {
        return exchangeRates;
    }

    @Override
    @Transactional
    public void lockRates(String vendorAgentId) throws NoSuchFieldException {
        VendorRatesData vendorRatesData = getUnlockedRatesByVendorId(vendorAgentId);
        if (ObjectUtils.isEmpty(vendorRatesData)) {
            throw new NoSuchFieldException("Rates already locked");
        }
        vendorRatesData.setLocked(true);
        vendorRatesRepository.save(vendorRatesData);
    }

    @Override
    public Object goLive() {
        return null;
    }

    @Override
    public List<VendorRatesData> getRatesByVendorId(String vendorAgentId) {
        return vendorRatesRepository.getByVendorAgentId(vendorAgentId);
    }

    @Override
    public List<VendorRatesData> getRatesByVendorIdAndDate(String vendorAgentId, LocalDate date) {
        return vendorRatesRepository.getVendorRatesByDate(vendorAgentId,
                date.atStartOfDay().toString(),
                date.atTime(LocalTime.MAX).truncatedTo(ChronoUnit.SECONDS).toString());
    }

    @Override
    public List<VendorRatesData> getVendorRates() {
        return vendorRatesRepository.findAll();
    }

    @Override
    @Transactional
    public void updateVendorRates(VendorRatesDTO vendorRates) throws InvalidPropertiesFormatException {
        VendorRatesData vendorRatesData = mapToData(vendorRates);
        vendorRatesData.setModifiedDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
        vendorRatesRepository.save(vendorRatesData);
    }

    @Override
    @Transactional
    public void saveVendorRates(VendorRatesDTO vendorRates) throws InvalidPropertiesFormatException {
        VendorRatesData vendorRatesData = mapToData(vendorRates);
        vendorRatesData.setCreateDate(LocalDateTime.now(ZoneId.of(ZONE)).toString());
        vendorRatesRepository.save(vendorRatesData);
    }

    @Override
    public VendorRatesData getUnlockedRatesByVendorId(String vendorAgentId) {
        return vendorRatesRepository.getByVendorAgentIdAndLocked(vendorAgentId, false);
    }

    private VendorRatesData mapToData(VendorRatesDTO vendorRatesDTO) throws InvalidPropertiesFormatException {
        VendorRatesData vendorRatesData = new VendorRatesData();

        try {
            vendorRatesData.setRatesData(mapper.writeValueAsString(vendorRatesDTO.getForexRequests()));
        } catch (JsonProcessingException e) {
            throw new InvalidPropertiesFormatException("Error fetching rates from request");
        }
        vendorRatesData.setVendorAgentId(vendorRatesDTO.getVendorAgentId());
        vendorRatesData.setVendorName(vendorRatesDTO.getVendorName());

        return vendorRatesData;
    }
}
