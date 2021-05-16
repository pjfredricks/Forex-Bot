package com.forexbot.api.web.rates;

import com.forexbot.api.dao.rates.ForexData;
import com.forexbot.api.service.RatesServiceV2;
import com.forexbot.api.web.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.forexbot.api.web.utils.ResponseWrapper.buildSuccessResponse;

@Slf4j
@RestController
@RequestMapping("api/v2")
@RequiredArgsConstructor
public class RatesControllerV2 {

    private final RatesServiceV2 ratesServiceV2;

    @GetMapping(path = "/rates")
    public ResponseEntity<ResponseWrapper> getExchangeRates() {
        return ResponseEntity.ok(
                buildSuccessResponse("Rates have been fetched", ratesServiceV2.getExchangeRates())
        );
    }

    @PostMapping(path = "/goLive")
    public ResponseEntity<ResponseWrapper> goLive() {
        return ResponseEntity.ok(
                buildSuccessResponse("Rates have been fetched", ratesServiceV2.getExchangeRates())
        );
    }

    @PostMapping(path = "/update/rates")
    public ResponseEntity<ResponseWrapper> updateExchangeRates(@RequestBody List<ForexData> ratesRequest,
                                                               @RequestHeader String approvedBy) {
        ratesServiceV2.updateRates(ratesRequest, approvedBy);
        return ResponseEntity.ok(
                buildSuccessResponse("Rates have been updated", null)
        );
    }
}
