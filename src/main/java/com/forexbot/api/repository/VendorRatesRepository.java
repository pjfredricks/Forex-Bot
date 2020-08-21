package com.forexbot.api.repository;

import com.forexbot.api.dao.rates.VendorRatesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRatesRepository extends JpaRepository<VendorRatesData, Integer> {

    List<VendorRatesData> getByVendorAgentId(String vendorAgentId);

    @Query(value = "SELECT * FROM vendor_rates_data v " +
            "WHERE v.vendor_agent_id = :vendorAgentId " +
            "AND v.create_date BETWEEN :startDate AND :endDate", nativeQuery = true)
    List<VendorRatesData> getVendorRatesByDate(String vendorAgentId, String startDate, String endDate);

    VendorRatesData getByVendorAgentIdAndLocked(String vendorAgentId, boolean locked);
}