package com.example.demo.repository;

import com.example.demo.repository.dao.userdata.OtpData;
import com.example.demo.repository.dao.userdata.OtpType;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface OtpDataRepository extends JpaRepository<OtpData, Integer> {

    List<OtpData> findAll();

    OtpData save(OtpData otpData);

    OtpData findOtpDataByEmailIdAndOtpType(String emailId, OtpType otpType);
}