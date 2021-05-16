package com.forexbot.api.repository;

import com.forexbot.api.dao.backoffice.vendor.VendorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface VendorRepository extends JpaRepository<VendorData, Integer> {

    VendorData getVendorDataByVendorAgentId(String vendorAgentId);
}
