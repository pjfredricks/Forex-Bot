package com.forexbot.api.repository;

import com.forexbot.api.dao.rates.RatesData;
import com.forexbot.api.dao.rates.TempRatesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatesRepository extends JpaRepository<RatesData, Integer> {
    List<RatesData> findAll();

    RatesData save(TempRatesData ratesData);

    RatesData findAllByCreateDate(String date);
}