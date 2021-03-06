package com.forexbot.api.repository;

import com.forexbot.api.dao.rates.RatesData;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface RatesRepository extends JpaRepository<RatesData, Integer> {
    RatesData findAllByCreateDate(String date);
}
