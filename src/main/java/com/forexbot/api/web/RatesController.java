package com.forexbot.api.web;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.forexbot.api.dao.rates.ForexRequest;
import com.forexbot.api.service.RatesService;
import com.forexbot.api.web.utils.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.forexbot.api.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("api/v1")
public class RatesController {

    private final RatesService ratesService;

    public RatesController(RatesService ratesService) {
        this.ratesService = ratesService;
    }

    @PutMapping(path = "/rates")
    public ResponseEntity<ResponseWrapper> updateExchangeRates() {
        ratesService.updateRates("Api call");
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Rates have been updated",
                null), HttpStatus.OK);
    }

    @GetMapping(path = "/rates")
    public ResponseEntity<ResponseWrapper> getExchangeRates() {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Rates have been fetched",
                ratesService.getExchangeRates()), HttpStatus.OK);
    }

    @PutMapping(path = "/update/rates")
    public ResponseEntity<ResponseWrapper> updateExchangeRatesManual(@RequestBody List<ForexRequest> ratesRequest,
                                                                     @RequestHeader String triggerIdentity) {
        ratesService.updateRates(ratesRequest, triggerIdentity);
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Rates have been updated",
                null), HttpStatus.OK);
    }
}