package com.forexbot.api.repository;

import com.forexbot.api.dao.rates.TempRatesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TempRatesRepository extends JpaRepository<TempRatesData, Integer> {

    TempRatesData save(TempRatesData ratesData);
}