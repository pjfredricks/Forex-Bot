package com.example.demo.web;

import com.example.demo.repository.dao.ResponseWrapper;
import com.example.demo.service.RatesService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("api/v1")
public class RatesController {

    private RatesService ratesService;

    public RatesController(RatesService ratesService) {
        this.ratesService = ratesService;
    }

    @PutMapping(path = "/rates")
    public ResponseEntity<ResponseWrapper> getExchangeRates() {
        ratesService.updateExchangeRates();
        return new ResponseEntity<>(new ResponseWrapper(
                "SUCCESS",
                "Rates have been updated",
                null), HttpStatus.OK);
    }

    @GetMapping(path = "/rates")
    public ResponseEntity<ResponseWrapper> getExchangeRatesFromDb() {
        return new ResponseEntity<>(new ResponseWrapper(
                "SUCCESS",
                "Rates have been fetched",
                ratesService.getExchangeRates()), HttpStatus.OK);
    }

}