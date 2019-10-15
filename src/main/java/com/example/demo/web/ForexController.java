package com.example.demo.web;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.repository.dao.ForexModel;
import com.example.demo.service.ForexService;

@RestController
@RequestMapping("/api")
public class ForexController {
	
	@Autowired
	private ForexService forexService;
	
	@PutMapping(path = "/rates")
    public ResponseEntity<String> getExchangeRates() throws Exception {
		forexService.updateExchangeRates();
        return new ResponseEntity<>(HttpStatus.CREATED);
    }
	
	@GetMapping(path = "/rates")
    public ResponseEntity<List<ForexModel>> getExchangeRatesFromDb() throws Exception {
        return new ResponseEntity<>(forexService.getExchangeRates(), HttpStatus.OK);
    }
}