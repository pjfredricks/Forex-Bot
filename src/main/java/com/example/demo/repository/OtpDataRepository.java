package com.example.demo.repository;

import com.example.demo.repository.dao.otp.OtpData;
import com.example.demo.repository.dao.otp.OtpType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpDataRepository extends JpaRepository<OtpData, Integer> {

    List<OtpData> findAll();

    OtpData save(OtpData otpData);

    OtpData findOtpDataByMobileNumberAndOtpType(String mobileNumber, OtpType otpType);

    OtpData findOtpDataByMobileNumber(String mobileNumber);
}