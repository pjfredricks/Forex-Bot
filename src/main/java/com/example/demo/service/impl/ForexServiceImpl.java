package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.repository.dao.ForexModel;
import com.example.demo.service.ForexService;

@Service
public class ForexServiceImpl implements ForexService {

	//TODO : need to store this in DB and schedule it to run every 24 hrs
	@Override
	public Map<String, BigDecimal> getExchangeRates() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ForexModel> response = restTemplate
				.getForEntity("https://api.exchangerate-api.com/v4/latest/INR", ForexModel.class);
		
		Map<String, BigDecimal> responseMap = response.getBody().getRates();

		responseMap.entrySet().forEach(entry -> entry.setValue(
				BigDecimal.valueOf(1 / entry.getValue().doubleValue()).setScale(4, BigDecimal.ROUND_HALF_EVEN)));

		return responseMap;
	}
	
}