package com.example.demo.web;

import com.example.demo.repository.dao.ForexModel;
import com.example.demo.repository.dao.UserDetail;
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
        return new ResponseEntity<>(HttpStatus.CREATED);
    }

    @GetMapping(path = "/rates")
    public ResponseEntity<List<ForexModel>> getExchangeRatesFromDb() {
        return new ResponseEntity<>(forexService.getExchangeRates(), HttpStatus.OK);
    }

    @PostMapping(path = "/signUp")
    public ResponseEntity<String> signUpUser(@RequestBody UserDetail userDetail) {
        if (userDetail.getEmailId().matches(EMAIL_REGEX)) {
            forexService.signUpUser(userDetail);
            return new ResponseEntity<>(HttpStatus.CREATED);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/login")
    public ResponseEntity<String> login(@RequestParam String emailId,
                                        @RequestParam String password) {
        if (forexService.login(emailId, password)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
    }

    @GetMapping(path = "/checkUserExists")
    public ResponseEntity<String> checkUserExists(@RequestParam String emailId) {
        if (forexService.checkUserExists(emailId)) {
            return new ResponseEntity<>(HttpStatus.OK);
        }
        return new ResponseEntity<>(HttpStatus.NO_CONTENT);
    }
}