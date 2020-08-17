package com.forexbot.api.repository;

import com.forexbot.api.dao.customer.CustomerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerData, Integer> {

    CustomerData getCustomerDataByEmailId(String emailId);

    CustomerData getCustomerDataByMobileNum(String mobileNum);

    boolean existsByMobileNumOrEmailId(String emailId, String mobileNum);

    CustomerData getCustomerDataByEmailIdOrMobileNum(String emailId, String mobileNum);

    CustomerData getCustomerDataByEmailIdOrMobileNumAndActive(String emailId, String mobileNum, boolean isActive);

    CustomerData getCustomerDataByCustomerId(UUID customerId);

    List<CustomerData> findAll();

    CustomerData save(CustomerData customerData);
}