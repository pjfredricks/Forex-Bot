package com.example.demo.web;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.example.demo.service.ForexService;

@RestController
@RequestMapping("/api")
public class ForexController {
	
	@Autowired
	private ForexService forexService;
	
	@GetMapping
    public ResponseEntity<Map<String, BigDecimal>> getExchangeRates() throws Exception {
        return new ResponseEntity<>(forexService.getExchangeRates(), HttpStatus.OK);
    }
}