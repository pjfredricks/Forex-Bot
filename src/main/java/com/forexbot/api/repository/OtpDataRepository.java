package com.forexbot.api.repository;

import com.forexbot.api.dao.otp.OtpData;
import com.forexbot.api.dao.otp.OtpType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpDataRepository extends JpaRepository<OtpData, Integer> {

    List<OtpData> findAll();

    OtpData save(OtpData otpData);

    OtpData findOtpDataByMobileNumAndOtpType(String mobileNum, OtpType otpType);

    OtpData findOtpDataByMobileNum(String mobileNum);
}