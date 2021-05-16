package com.forexbot.api.repository;

import com.forexbot.api.dao.otp.OtpData;
import com.forexbot.api.dao.otp.OtpType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface OtpDataRepository extends JpaRepository<OtpData, Integer> {
    Optional<OtpData> findOtpDataByMobileNumAndOtpType(String mobileNum, OtpType otpType);

    Optional<OtpData> findOtpDataByMobileNum(String mobileNum);
}
