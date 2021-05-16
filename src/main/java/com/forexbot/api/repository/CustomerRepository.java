package com.forexbot.api.repository;

import com.forexbot.api.dao.customer.CustomerData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface CustomerRepository extends JpaRepository<CustomerData, Integer> {

    CustomerData getCustomerDataByEmailId(String emailId);

    CustomerData getCustomerDataByMobileNum(String mobileNum);

    boolean existsByMobileNumOrEmailId(String emailId, String mobileNum);

    CustomerData getCustomerDataByEmailIdOrMobileNum(String emailId, String mobileNum);

    @Query(value = "SELECT * FROM customer_details cd WHERE (cd.email_id = :emailId or cd.mobile_num = :mobileNum) and cd.is_active = true", nativeQuery = true)
    CustomerData getCustomerDataByEmailIdOrMobileNumAndIsActive(@Param("emailId") String emailId, @Param("mobileNum") String mobileNum);

    CustomerData getCustomerDataByCustomerId(UUID customerId);
}
