package com.example.demo.service.impl;

import com.example.demo.repository.ForexRepository;
import com.example.demo.repository.dao.ForexModel;
import com.example.demo.repository.dao.UserData;
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
                BigDecimal.valueOf(1 / entry.getValue().doubleValue()).setScale(3, RoundingMode.HALF_EVEN)));
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
    public String signUpUser(UserData userData) throws Exception {
        userData.setPassword(bCryptPasswordEncoder.encode(userData.getPassword()));
        userData.setCreate_date(LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toString());
        return String.valueOf(repository.save(userData).getId());
    }

    @Override
    public Boolean login(String userName, String emailId, String password) throws Exception {
        UserData userData = StringUtils.isEmpty(userName) ? getUserDataByEmail(emailId) : getUserDataByUserName(userName);
        return checkPasswordsMatch(password, userData.getPassword());
    }

    private UserData getUserDataByUserName(String userName) {
        return repository.findUserDetailByName(userName);
    }

    private UserData getUserDataByEmail(String emailId) {
        return repository.findUserDetailByEmailId(emailId);
    }

    private Boolean checkPasswordsMatch(String enteredPassword, String passwordFromDb) {
        return bCryptPasswordEncoder.matches(enteredPassword, passwordFromDb);
    }

    // TODO: add update password
    private UserData updatePassword(UserData userData) {
        UserData userDataFromDb = StringUtils.isEmpty(userData.getName())
                ? getUserDataByEmail(userData.getEmailId()) : getUserDataByUserName(userData.getName());

        if (checkPasswordsMatch(userData.getPassword(), userDataFromDb.getPassword())) {
            // Executed only when user entered password and db password match
            userData.setPassword(bCryptPasswordEncoder.encode(userData.getPassword()));
            userData.setModified_date(LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toString());
            return userData;
        } else {
            // User password does not match
            return null;
        }
    }
}