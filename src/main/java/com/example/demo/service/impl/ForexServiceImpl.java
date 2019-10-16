package com.example.demo.service.impl;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.example.demo.repository.ForexRepository;
import com.example.demo.repository.dao.ForexModel;
import com.example.demo.service.ForexService;

@Service
public class ForexServiceImpl implements ForexService {

	private static final Logger logger = LoggerFactory.getLogger(ForexServiceImpl.class);

	private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

	@Autowired
	ForexRepository repository;

	private static Optional<ForexModel> forexValue = getExchangeRateResponse();

	@Scheduled(cron = "0 0 2 * * *")
	public void testScheduleTask() {
		logger.info("Updating Database with latest Rates @ Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
		forexValue = getExchangeRateResponse();
		updateExchangeRates();
	}

	@Override
	public void updateExchangeRates() {
		Map<String, BigDecimal> responseMap = forexValue.get().getRates();

		responseMap.entrySet().forEach(entry -> entry.setValue(
				BigDecimal.valueOf(1 / entry.getValue().doubleValue()).setScale(4, BigDecimal.ROUND_HALF_EVEN)));

		forexValue.get().getRates().entrySet().forEach(rate -> {
			rate.setValue(responseMap.get(rate.getKey()));
		});
	}

	private static Optional<ForexModel> getExchangeRateResponse() {
		RestTemplate restTemplate = new RestTemplate();
		ResponseEntity<ForexModel> response = null;
		try {
			response = restTemplate
					.getForEntity("https://api.exchangerate-api.com/v4/lates/INR", ForexModel.class);
			
		} catch(Exception e) {
			logger.error("Error fetching data from exchange rate api, trying again after 5 minutes");
		}
		
		return Optional.of(response.getBody());
	}

	@Override
	public List<ForexModel> getExchangeRates() {
		if (!forexValue.isPresent()) {
			return new ArrayList<ForexModel>();
		}
		return Arrays.asList(forexValue.get());
	}

}