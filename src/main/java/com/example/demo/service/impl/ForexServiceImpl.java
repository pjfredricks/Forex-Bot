package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.repository.ForexRepository;
import com.example.demo.repository.dao.ForexModel;
import com.example.demo.service.ForexService;

@Service
public class ForexServiceImpl implements ForexService {
	
	@Autowired
	ForexRepository repository;

	//TODO : need to store this in DB and schedule it to run every 24 hrs
	@Override
	public void updateExchangeRates() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ForexModel> response = restTemplate
				.getForEntity("https://api.exchangerate-api.com/v4/latest/INR", ForexModel.class);
		
		Map<String, BigDecimal> responseMap = response.getBody().getRates();

		responseMap.entrySet().forEach(entry -> entry.setValue(
				BigDecimal.valueOf(1 / entry.getValue().doubleValue()).setScale(4, BigDecimal.ROUND_HALF_EVEN)));
		
		repository.deleteAll();
		repository.save(response.getBody());
	}
	
	@Override
	public List<ForexModel> getExchangeRates() {
		return repository.findAll();
	}
	
}