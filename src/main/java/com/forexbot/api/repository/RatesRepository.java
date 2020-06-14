package com.forexbot.api.repository;

import com.forexbot.api.dao.rates.DailyRatesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface RatesRepository extends JpaRepository<DailyRatesData, Integer> {
    List<DailyRatesData> findAll();

    DailyRatesData save(DailyRatesData ratesData);

    DailyRatesData getAllByCreateDate(String date);
}