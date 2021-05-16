package com.forexbot.api.service.impl;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.core.type.TypeReference;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.forexbot.api.dao.rates.ForexRates;
import com.forexbot.api.dao.rates.VendorRatesDTO;
import com.forexbot.api.dao.rates.VendorRatesData;
import com.forexbot.api.repository.VendorRatesRepository;
import com.forexbot.api.service.VendorRatesService;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.beans.BeanUtils;
import org.springframework.beans.factory.annotation.Qualifier;
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

    public VendorRatesServiceImpl(VendorRatesRepository vendorRatesRepository,
                                  ObjectMapper mapper) {
        this.vendorRatesRepository = vendorRatesRepository;
        this.mapper = mapper;
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
    public List<VendorRatesDTO> getRatesByVendorId(String vendorAgentId) {
        List<VendorRatesData> vendorRatesData = vendorRatesRepository.getByVendorAgentId(vendorAgentId);
        return mapToResponse(vendorRatesData);
    }

    @Override
    public List<VendorRatesDTO> getRatesByVendorIdAndDate(String vendorAgentId, LocalDate date) {
        List<VendorRatesData> vendorRatesData = vendorRatesRepository.getVendorRatesByDate(vendorAgentId,
                date.atStartOfDay().toString(),
                date.atTime(LocalTime.MAX).truncatedTo(ChronoUnit.SECONDS).toString());
        return mapToResponse(vendorRatesData);
    }

    @Override
    public List<VendorRatesDTO> getVendorRates() {
        List<VendorRatesData> vendorRatesData = vendorRatesRepository.findAll();
        return mapToResponse(vendorRatesData);
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

    private List<VendorRatesDTO> mapToResponse(List<VendorRatesData> vendorRates) {
        List<VendorRatesDTO> vendorRatesDTOS = new ArrayList<>();
        vendorRates.forEach(vendorRateData -> {
            VendorRatesDTO vendorRatesDTO = new VendorRatesDTO();
            BeanUtils.copyProperties(vendorRateData, vendorRatesDTO, "forexRequests");
            try {
                vendorRatesDTO.setForexRequests(mapper.readValue(vendorRateData.getRatesData(), new TypeReference<>() {
                }));
            } catch (JsonProcessingException e) {
                vendorRatesDTO.setForexRequests(new ArrayList<>());
            }
            vendorRatesDTOS.add(vendorRatesDTO);
        });

        return vendorRatesDTOS;
    }
}
