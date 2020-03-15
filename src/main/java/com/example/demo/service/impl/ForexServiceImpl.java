package com.example.demo.service.impl;

import com.example.demo.repository.ForexRepository;
import com.example.demo.repository.dao.ForexModel;
import com.example.demo.repository.dao.UserDetail;
import com.example.demo.service.ForexService;
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
import java.time.LocalDateTime;
import java.time.ZoneId;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

@Service
public class ForexServiceImpl implements ForexService {

    private static final Logger logger = LoggerFactory.getLogger(ForexServiceImpl.class);

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final ForexRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static Optional<ForexModel> forexValue = getExchangeRateResponse();

    public ForexServiceImpl(ForexRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void onStartup() {
        updateExchangeRates();
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void scheduledTask() {
        logger.info("Updating Database with latest Rates @ Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        forexValue = getExchangeRateResponse();
        updateExchangeRates();
    }

    @Override
    public void updateExchangeRates() {
        forexValue.get().getRates().entrySet().forEach(entry -> entry.setValue(
                BigDecimal.valueOf(1 / entry.getValue().doubleValue()).setScale(4, BigDecimal.ROUND_HALF_EVEN)));
    }

    private static Optional<ForexModel> getExchangeRateResponse() {
        RestTemplate restTemplate = new RestTemplate();
        ResponseEntity<ForexModel> response = null;
        try {
            response = restTemplate
                    .getForEntity("https://api.exchangerate-api.com/v4/latest/INR", ForexModel.class);
        } catch (Exception e) {
            logger.error("Error fetching data from exchange rate api @ Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        }

        return Optional.of(response.getBody());
    }

    @Override
    public List<ForexModel> getExchangeRates() {
        if (!forexValue.isPresent()) {
            return new ArrayList<>();
        }
        return Arrays.asList(forexValue.get());
    }

    @Override
    @Transactional
    public Boolean signUpUser(UserDetail userDetail) {
        if (checkUserExists(userDetail.getEmailId())) {
            return modifyExistingUser(userDetail);
        }
        userDetail.setPassword(bCryptPasswordEncoder.encode(userDetail.getPassword()));
        userDetail.setCreate_date(LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toString());
        repository.save(userDetail);
        return true;
    }

    @Override
    public Boolean login(String emailId, String password) {
        if (checkUserExists(emailId)) {
            return checkPasswordsMatch(password, getPasswordFromDb(emailId));
        }
        return false;
    }

    @Override
    public Boolean checkUserExists(String emailId) {
        return repository.existsByEmailId(emailId);
    }

    private Boolean checkPasswordsMatch(String enteredPassword, String passwordFromDb) {
        return bCryptPasswordEncoder.matches(enteredPassword, passwordFromDb);
    }

    private String getPasswordFromDb(String emailId) {
        return repository.findUserDetailByEmailId(emailId).getPassword();
    }

    private Boolean modifyExistingUser(UserDetail userDetail) {
        if (checkPasswordsMatch(
                userDetail.getPassword(),
                getPasswordFromDb(userDetail.getEmailId()))) {
            // Executed only when user entered password and db password match
            userDetail.setPassword(bCryptPasswordEncoder.encode(userDetail.getPassword()));
            userDetail.setModified_date(LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toString());
            return true;
        } else {
            // User password does not match
            return false;
        }
    }
}