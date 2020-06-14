package com.forexbot.api.repository;

import com.forexbot.api.dao.admin.VendorData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VendorRepository extends JpaRepository<VendorData, Integer> {
    List<VendorData> findAll();

    VendorData save(VendorData vendorData);

    VendorData getVendorDataByUserName(String userName);
}