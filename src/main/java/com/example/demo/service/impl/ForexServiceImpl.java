package com.example.demo.service.impl;

import com.example.demo.repository.ForexRepository;
import com.example.demo.repository.dao.ForexRates;
import com.example.demo.repository.dao.UserDataRequest;
import com.example.demo.repository.dao.UserDataResponse;
import com.example.demo.service.ForexService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import javax.annotation.PostConstruct;
import javax.transaction.Transactional;
import java.math.BigDecimal;
import java.math.RoundingMode;
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.*;
import java.util.stream.Collectors;

@Service
public class ForexServiceImpl implements ForexService {

    private static final Logger logger = LoggerFactory.getLogger(ForexServiceImpl.class);

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final ForexRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static Map<String, Double> forexValue = getExchangeRateResponse();
    private static List<ForexRates> rates;

    public ForexServiceImpl(ForexRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void onStartup() {
        rates = updateExchangeRates();
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void scheduledTask() {
        logger.info("Updating Database with latest Rates @ Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        forexValue = getExchangeRateResponse();
        rates = updateExchangeRates();
    }

    @Override
    public List<ForexRates> updateExchangeRates() {
        ForexRates rate = new ForexRates();
        forexValue.remove("INR");
        return forexValue.entrySet()
                .stream()
                .map(forexValue -> new ForexRates(Currency.getInstance(forexValue.getKey()).getDisplayName(),
                        BigDecimal.valueOf(1 / forexValue.getValue()).setScale(3, RoundingMode.HALF_EVEN),
                        null))
                .collect(Collectors.toList());
    }

    private static Map<String, Double> getExchangeRateResponse() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<LinkedHashMap> response = null;
        try {
            response = restTemplate
                    .getForEntity("https://api.exchangerate-api.com/v4/latest/INR", LinkedHashMap.class);
        } catch (Exception e) {
            logger.error("Error fetching data from exchange rate api @ Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        }

        return (Map<String, Double>) response.getBody().get("rates");
    }

    @Override
    public List<ForexRates> getExchangeRates() {
        if (rates.isEmpty()) {
            return new ArrayList<>();
        }
        return rates;
    }

    @Override
    @Transactional
    public UserDataResponse signUpUser(UserDataRequest userDataRequest) throws Exception {
        userDataRequest.setPassword(bCryptPasswordEncoder.encode(userDataRequest.getPassword()));
        userDataRequest.setCreate_date(LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toString());
        userDataRequest.setId(repository.save(userDataRequest).getId());
        return UserDataResponse.map(userDataRequest);
    }

    @Override
    public UserDataResponse login(String userName, String emailId, String password) throws Exception {
        UserDataRequest userDataRequest = StringUtils.isEmpty(userName) ? getUserDataByEmail(emailId) : getUserDataByUserName(userName);
        if (checkPasswordsMatch(password, userDataRequest.getPassword())) {
            return UserDataResponse.map(userDataRequest);
        }
        return null;
    }

    private UserDataRequest getUserDataByUserName(String userName) {
        return repository.findUserDetailByName(userName);
    }

    private UserDataRequest getUserDataByEmail(String emailId) {
        return repository.findUserDetailByEmailId(emailId);
    }

    private Boolean checkPasswordsMatch(String enteredPassword, String passwordFromDb) {
        return bCryptPasswordEncoder.matches(enteredPassword, passwordFromDb);
    }

    // TODO: add update password
    private UserDataRequest updatePassword(UserDataRequest userDataRequest) {
        UserDataRequest userDataRequestFromDb = StringUtils.isEmpty(userDataRequest.getName())
                ? getUserDataByEmail(userDataRequest.getEmailId()) : getUserDataByUserName(userDataRequest.getName());

        if (checkPasswordsMatch(userDataRequest.getPassword(), userDataRequestFromDb.getPassword())) {
            // Executed only when user entered password and db password match
            userDataRequest.setPassword(bCryptPasswordEncoder.encode(userDataRequest.getPassword()));
            userDataRequest.setModified_date(LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toString());
            return userDataRequest;
        } else {
            // User password does not match
            return null;
        }
    }
}