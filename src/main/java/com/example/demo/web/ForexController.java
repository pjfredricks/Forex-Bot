package com.example.demo.web;

import com.example.demo.repository.dao.ResponseWrapper;
import com.example.demo.repository.dao.UserDataRequest;
import com.example.demo.repository.dao.UserDataResponse;
import com.example.demo.service.ForexService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api")
public class ForexController {

    private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    private final ForexService forexService;

    public ForexController(ForexService forexService) {
        this.forexService = forexService;
    }

    @PutMapping(path = "/update/rates")
    public ResponseEntity<ResponseWrapper> getExchangeRates() {
        forexService.updateExchangeRates();
        return new ResponseEntity<>(new ResponseWrapper("SUCCESS", "Rates have been updated", null), HttpStatus.OK);
    }

    @GetMapping(path = "/rates")
    public ResponseEntity<ResponseWrapper> getExchangeRatesFromDb() {
        return new ResponseEntity<>(new ResponseWrapper("SUCCESS", "Rates have been fetched", forexService.getExchangeRates()), HttpStatus.OK);
    }

    @PostMapping(path = "/signUp")
    public ResponseEntity<ResponseWrapper> signUpUser(@RequestBody UserDataRequest userDataRequest) {
        if (userDataRequest.getEmailId().matches(EMAIL_REGEX)) {
            try {
                return new ResponseEntity<>(new ResponseWrapper("SUCCESS", "User signed Up successfully", forexService.signUpUser(userDataRequest)), HttpStatus.OK);
            } catch (Exception e) {
                return new ResponseEntity<>(new ResponseWrapper("ERROR", "Invalid Details, user already exists", null), HttpStatus.OK);
            }
        }
        return new ResponseEntity<>(new ResponseWrapper("SUCCESS", "Invalid Email", null), HttpStatus.OK);
    }

    @PostMapping(path = "/login")
    public ResponseEntity<ResponseWrapper> login(@RequestBody UserDataRequest userDataRequest) {
        UserDataResponse userDataResponse = null;
        try {
            userDataResponse = forexService.login(userDataRequest.getName(), userDataRequest.getEmailId(), userDataRequest.getPassword());
        } catch (Exception e) {
            e.printStackTrace();
        }
        if (userDataResponse != null) {
                return new ResponseEntity<>(new ResponseWrapper("SUCCESS", "Login success", userDataResponse), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseWrapper("ERROR", "Incorrect Password", null), HttpStatus.OK);
    }
}