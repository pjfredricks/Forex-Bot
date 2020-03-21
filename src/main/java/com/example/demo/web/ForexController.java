package com.example.demo.web;

import com.example.demo.repository.dao.ForexModel;
import com.example.demo.repository.dao.UserData;
import com.example.demo.service.ForexService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api")
public class ForexController {

    private static final String EMAIL_REGEX = "^[\\w-_\\.+]*[\\w-_\\.]\\@([\\w]+\\.)+[\\w]+[\\w]$";

    private final ForexService forexService;

    public ForexController(ForexService forexService) {
        this.forexService = forexService;
    }

    @PutMapping(path = "/update/rates")
    public ResponseEntity<String> getExchangeRates() {
        forexService.updateExchangeRates();
        return new ResponseEntity<>("Rates have been updated", HttpStatus.CREATED);
    }

    @GetMapping(path = "/rates")
    public ResponseEntity<List<ForexModel>> getExchangeRatesFromDb() {
        return new ResponseEntity<>(forexService.getExchangeRates(), HttpStatus.OK);
    }

    @PostMapping(path = "/signUp")
    public ResponseEntity<String> signUpUser(@RequestBody UserData userData) {
        if (userData.getEmailId().matches(EMAIL_REGEX)) {
            try {
                return new ResponseEntity<>(forexService.signUpUser(userData), HttpStatus.CREATED);
            } catch (Exception e) {
                return new ResponseEntity<>("Invalid Details, user already exists", HttpStatus.BAD_REQUEST);
            }
        }
        return new ResponseEntity<>("Invalid Email", HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/login")
    public ResponseEntity<String> login(@RequestParam(required = false) String userName,
                                        @RequestParam(required = false) String emailId,
                                        @RequestParam String password) {
        try {
            if (forexService.login(userName, emailId, password)) {
                return new ResponseEntity<>("Login success", HttpStatus.OK);
            }
            return new ResponseEntity<>("Incorrect Password", HttpStatus.BAD_REQUEST);
        } catch (Exception e) {
            return new ResponseEntity<>("Error, details do not exist", HttpStatus.BAD_REQUEST);
        }
    }
}