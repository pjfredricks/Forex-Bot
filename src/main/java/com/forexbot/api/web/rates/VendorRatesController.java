package com.forexbot.api.web.rates;

import com.forexbot.api.dao.rates.VendorRatesDTO;
import com.forexbot.api.service.VendorRatesService;
import com.forexbot.api.web.utils.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;

import static com.forexbot.api.web.utils.Constants.*;

@RestController
@RequestMapping("api/v1")
public class VendorRatesController {

    private final VendorRatesService vendorRatesService;

    public VendorRatesController(VendorRatesService vendorRatesService) {
        this.vendorRatesService = vendorRatesService;
    }

    @PostMapping(path = "/lockRates/{vendorAgentId}")
    public ResponseEntity<ResponseWrapper> lockRates(@PathVariable String vendorAgentId) {
        try {
            vendorRatesService.lockRates(vendorAgentId);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    e.getMessage(),
                    null), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Rates have been locked",
                null), HttpStatus.OK);
    }

    @PostMapping(path = "/saveVendorRates")
    public ResponseEntity<ResponseWrapper> saveRates(@RequestBody VendorRatesDTO vendorRates) {
        try {
            vendorRatesService.saveVendorRates(vendorRates);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    e.getMessage(),
                    null), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Rates have been saved",
                null), HttpStatus.OK);
    }

    @PutMapping(path = "/updateVendorRates")
    public ResponseEntity<ResponseWrapper> updateRates(@RequestBody VendorRatesDTO vendorRates) {
        try {
            vendorRatesService.updateVendorRates(vendorRates);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    e.getMessage(),
                    null), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Rates have been updated",
                null), HttpStatus.OK);
    }

    @GetMapping(path = "/getCountries")
    public ResponseEntity<ResponseWrapper> getCountries() {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Rates have been updated",
                vendorRatesService.getCountries()), HttpStatus.OK);
    }

    @GetMapping(path = "/getVendorRates")
    public ResponseEntity<ResponseWrapper> getVendorRates() {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "All Vendor Rates have been fetched",
                vendorRatesService.getVendorRates()), HttpStatus.OK);
    }

    @GetMapping(path = "/rates/vendorAgentId/{vendorAgentId}")
    public ResponseEntity<ResponseWrapper> getRatesByVendorId(@PathVariable String vendorAgentId) {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Rates have been fetched for vendor",
                vendorRatesService.getRatesByVendorId(vendorAgentId)), HttpStatus.OK);
    }

    @GetMapping(path = "/rates/vendorAgentId/{vendorAgentId}/{date}")
    public ResponseEntity<ResponseWrapper> getRatesByVendorId(@PathVariable String vendorAgentId,
                                                              @PathVariable String date) {
        try {
            LocalDate d = LocalDate.parse(date, DateTimeFormatter.ofPattern("dd-MM-yyyy"));
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Rates have been fetched for vendor",
                    vendorRatesService.getRatesByVendorIdAndDate(vendorAgentId, d)), HttpStatus.OK);
        } catch (DateTimeParseException e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Invalid date format",
                    null), HttpStatus.OK);
        }
    }
}
