package com.forexbot.api.web.rates;

import com.forexbot.api.dao.rates.VendorRatesDTO;
import com.forexbot.api.service.VendorRatesService;
import com.forexbot.api.web.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.forexbot.api.web.utils.ResponseWrapper.*;

@Slf4j
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class VendorRatesController {

    private final VendorRatesService vendorRatesService;

    @PostMapping(path = "/saveVendorRates")
    public ResponseEntity<ResponseWrapper> saveRates(@RequestBody VendorRatesDTO vendorRates) {
        try {
            vendorRatesService.saveVendorRates(vendorRates);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(
                    buildErrorResponse(e.getMessage(), null)
            );
        }
        return ResponseEntity.ok(
                buildSuccessResponse("Rates have been saved", null)
        );
    }

    @PutMapping(path = "/updateVendorRates")
    public ResponseEntity<ResponseWrapper> updateRates(@RequestBody VendorRatesDTO vendorRates) {
        try {
            vendorRatesService.updateVendorRates(vendorRates);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(
                    buildErrorResponse(e.getMessage(), null)
            );
        }
        return ResponseEntity.ok(
                buildSuccessResponse("Rates have been updated", null)
        );
    }

    @GetMapping(path = "/getCountries")
    public ResponseEntity<ResponseWrapper> getCountries() {
        return ResponseEntity.ok(
                buildSuccessResponse("Rates have been updated", vendorRatesService.getCountries())
        );
    }

    @GetMapping(path = "/getVendorRates")
    public ResponseEntity<ResponseWrapper> getVendorRates() {
        return ResponseEntity.ok(
                buildSuccessResponse("All Vendor Rates have been fetched", vendorRatesService.getVendorRates())
        );
    }

    @GetMapping(path = "/rates/vendorAgentId/{vendorAgentId}")
    public ResponseEntity<ResponseWrapper> getRatesByVendorId(@PathVariable String vendorAgentId) {
        return ResponseEntity.ok(
                buildSuccessResponse("Rates have been fetched for vendor",
                        vendorRatesService.getRatesByVendorId(vendorAgentId))
        );
    }

    @GetMapping(path = "/rates/vendorAgentId/{vendorAgentId}/{date}")
    public ResponseEntity<ResponseWrapper> getRatesByVendorId(@PathVariable String vendorAgentId,
                                                              @PathVariable String date) {
        try {
            LocalDate d = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            return ResponseEntity.ok(
                    buildSuccessResponse("Rates have been fetched for vendor",
                            vendorRatesService.getRatesByVendorIdAndDate(vendorAgentId, d))
            );
        } catch (DateTimeParseException e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(
                    buildErrorResponse("Invalid date format", null)
            );
        }
    }
}
