package com.forexbot.api.service;

import com.forexbot.api.dao.rates.ForexRates;
import com.forexbot.api.dao.rates.VendorRatesDTO;
import com.forexbot.api.dao.rates.VendorRatesData;

import java.time.LocalDate;
import java.util.InvalidPropertiesFormatException;
import java.util.List;

public interface VendorRatesService {

    List<ForexRates> getCountries();

    void lockRates(String vendorAgentId) throws NoSuchFieldException;

    void saveVendorRates(VendorRatesDTO vendorRates) throws InvalidPropertiesFormatException;

    void updateVendorRates(VendorRatesDTO vendorRates) throws InvalidPropertiesFormatException;

    Object goLive();

    List<VendorRatesData> getRatesByVendorId(String vendorAgentId);

    List<VendorRatesData> getRatesByVendorIdAndDate(String vendorAgentId, LocalDate date);

    List<VendorRatesData> getVendorRates();

    VendorRatesData getUnlockedRatesByVendorId(String vendorAgentId);
}