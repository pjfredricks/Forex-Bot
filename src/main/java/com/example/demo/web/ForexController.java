package com.example.demo.web;

import com.example.demo.repository.dao.ResponseWrapper;
import com.example.demo.repository.dao.UserDataRequest;
import com.example.demo.service.ForexService;
import org.springframework.http.HttpStatus;
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
    public ResponseWrapper getExchangeRates() {
        forexService.updateExchangeRates();
        return new ResponseWrapper( HttpStatus.OK.getReasonPhrase(), "Rates have been updated", null);
    }

    @GetMapping(path = "/rates")
    public ResponseWrapper getExchangeRatesFromDb() {
        return new ResponseWrapper(HttpStatus.OK.getReasonPhrase(), "Rates have been fetched", forexService.getExchangeRates());
    }

    @PostMapping(path = "/signUp")
    public ResponseWrapper signUpUser(@RequestBody UserDataRequest userDataRequest) {
        if (userDataRequest.getEmailId().matches(EMAIL_REGEX)) {
            try {
                return new ResponseWrapper(HttpStatus.OK.getReasonPhrase(), "User signed Up successfully", forexService.signUpUser(userDataRequest));
            } catch (Exception e) {
                return new ResponseWrapper(HttpStatus.OK.getReasonPhrase(), "Invalid Details, user already exists", null);
            }
        }
        return new ResponseWrapper(HttpStatus.OK.getReasonPhrase(), "Invalid Email", null);
    }

    @PostMapping(path = "/login")
    public ResponseWrapper login(@RequestBody UserDataRequest userDataRequest) {
        try {
            return new ResponseWrapper(HttpStatus.OK.getReasonPhrase(), "Login success" , forexService.login(userDataRequest.getName(), userDataRequest.getEmailId(), userDataRequest.getPassword()));
        } catch (Exception e) {
            return new ResponseWrapper(HttpStatus.OK.getReasonPhrase(), "Incorrect Password", null);
        }
    }
}
