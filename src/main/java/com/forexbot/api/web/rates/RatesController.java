package com.forexbot.api.web.rates;

import com.forexbot.api.dao.rates.ForexData;
import com.forexbot.api.service.RatesService;
import com.forexbot.api.web.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.forexbot.api.web.utils.ResponseWrapper.buildSuccessResponse;

@Slf4j
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
@Deprecated
public class RatesController {

    private final RatesService ratesService;

    @PutMapping(path = "/rates")
    public ResponseEntity<ResponseWrapper> updateExchangeRates() {
        ratesService.updateRates("Api call");
        return ResponseEntity.ok(
                buildSuccessResponse("Rates have been updated", null)
        );
    }

    @GetMapping(path = "/rates")
    public ResponseEntity<ResponseWrapper> getExchangeRates() {
        return ResponseEntity.ok(
                buildSuccessResponse("Rates have been fetched", ratesService.getExchangeRates())
        );
    }

    @PutMapping(path = "/update/rates")
    public ResponseEntity<ResponseWrapper> updateExchangeRatesManual(@RequestBody List<ForexData> ratesRequest,
                                                                     @RequestHeader String approvedBy) {
        ratesService.updateRates(ratesRequest, approvedBy);
        return ResponseEntity.ok(
                buildSuccessResponse("Rates have been updated", null)
        );
    }

    @DeleteMapping(path = "/rates")
    public ResponseEntity<ResponseWrapper> deleteRates() {
        ratesService.deleteTempRates();
        return ResponseEntity.ok(
                buildSuccessResponse("Rates have been updated", null)
        );
    }
}
