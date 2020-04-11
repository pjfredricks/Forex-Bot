package com.example.demo.service.impl;

import com.example.demo.repository.ForexRepository;
import com.example.demo.repository.dao.ForexRates;
import com.example.demo.repository.dao.UserData;
import com.example.demo.repository.dao.UserDataRequest;
import com.example.demo.repository.dao.UserDataResponse;
import com.example.demo.service.ForexService;
import org.apache.commons.lang3.StringUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
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
import java.util.stream.Stream;

@Service
public class ForexServiceImpl implements ForexService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ForexServiceImpl.class);

    private static final DateTimeFormatter dateTimeFormatter = DateTimeFormatter.ofPattern("HH:mm:ss");

    private final ForexRepository repository;
    private final BCryptPasswordEncoder bCryptPasswordEncoder;

    private static Map<String, Double> currencyValues = new HashMap<>();
    private static List<ForexRates> forexRates = new ArrayList<>();
    private static final Set<String> countryList = Stream.of("USD", "AUD", "GBP", "EUR", "CAD",
            "CNY", "SAR", "SGD", "MYR", "THB",
            "IDR", "ILS", "JPY", "KRW", "CHF",
            "PHP", "FJD", "HKD", "ZAR").collect(Collectors.toSet());
    private static final Set<String> noCarouselCountryList = Stream.of("ILS", "JPY", "KRW", "CHF",
            "PHP", "FJD", "HKD", "ZAR").collect(Collectors.toSet());

    public ForexServiceImpl(ForexRepository repository, BCryptPasswordEncoder bCryptPasswordEncoder) {
        this.repository = repository;
        this.bCryptPasswordEncoder = bCryptPasswordEncoder;
    }

    @PostConstruct
    public void onStartup() {
        updateForexRates();
    }

    @Scheduled(cron = "0 0 2 * * *")
    public void scheduledTask() {
        LOGGER.info("Updating Database with latest Rates @ Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        updateForexRates();
    }

    @Override
    public void updateForexRates() {
        // Removes old values from both collections
        forexRates.clear();
        currencyValues.clear();

        // Updates latest forex currency values
        getCurrencyForexValues();

        currencyValues.keySet().retainAll(countryList);
        currencyValues.replaceAll((countryCode, currencyValue) -> convertRate(currencyValue));
        currencyValues.replaceAll((countryCode, currencyValue) -> applyReduction(currencyValue));

        forexRates = currencyValues.entrySet()
                .stream()
                .map(forexRate -> new ForexRates(true,
                        forexRate.getKey(),
                        Currency.getInstance(forexRate.getKey()).getDisplayName(),
                        forexRate.getValue(),
                        null))
                .collect(Collectors.toList());
        forexRates.forEach(forexRate -> {
            if (noCarouselCountryList.contains(forexRate.getCountryCode())) {
                forexRate.setCarousel(false);
            }
        });
    }

    @Override
    public List<ForexRates> getExchangeRates() {
        if (forexRates.isEmpty()) {
            updateForexRates();
        }
        return forexRates;
    }

    @Override
    @Transactional
    public UserDataResponse signUpUser(UserDataRequest userDataRequest) {
        UserData userData = mapRequestToData(userDataRequest);
        userData.setId(repository.save(userData).getId());

        return mapDataToResponse(userData);
    }

    @Override
    public UserDataResponse login(UserDataRequest userDataRequest) throws Exception {
        UserData userData = null;

        if (StringUtils.isEmpty(userDataRequest.getName())) {
            userData = getUserDataByEmailIdOrMobileNum(userDataRequest.getEmailId(), userDataRequest.getMobileNum());
        } else {
            userData = getUserDataByName(userDataRequest.getName());
        }

        if (checkPasswordsMatch(userDataRequest.getPassword(), userData.getPassword())) {
            return mapDataToResponse(userData);
        }
        return null;
    }

    private Double applyReduction(Double currencyValue) {
        int percent = 2;

        if (currencyValue < 5.000) percent = 6;
        else
        if (currencyValue < 30.000) percent = 5;
        else
        if (currencyValue < 80.000) percent = 3;

        currencyValue =  currencyValue - (currencyValue/100) * percent;
        return BigDecimal.valueOf(currencyValue).setScale(3, RoundingMode.HALF_EVEN).doubleValue();
    }

    private Double convertRate(Double currencyValue) {
        return BigDecimal.valueOf(1 / currencyValue).setScale(3, RoundingMode.HALF_EVEN).doubleValue();
    }

    private static void getCurrencyForexValues() {
        try {
            currencyValues = (Map<String, Double>) new RestTemplate()
                    .getForEntity("https://api.exchangerate-api.com/v4/latest/INR", LinkedHashMap.class)
                    .getBody()
                    .get("rates");
        } catch (Exception e) {
            LOGGER.error("Error fetching data from exchange rate api @ Time - {}", dateTimeFormatter.format(LocalDateTime.now()));
        }
    }

    private UserData getUserDataByName(String userName) {
        return repository.getUserDataByName(userName);
    }

    private UserData getUserDataByEmailIdOrMobileNum(String emailId, String mobileNum) {
        return repository.getUserDataByEmailIdOrMobileNum(emailId, mobileNum);
    }

    private Boolean checkPasswordsMatch(String enteredPassword, String passwordFromDb) {
        return bCryptPasswordEncoder.matches(enteredPassword, passwordFromDb);
    }

    private UserData mapRequestToData(UserDataRequest userRequest) {
        UserData userData = new UserData();

        userData.setUserId(UUID.randomUUID());
        userData.setEmailId(userRequest.getEmailId());
        userData.setMobileNum(userRequest.getMobileNum());
        userData.setName(userRequest.getName());
        userData.setPassword(bCryptPasswordEncoder.encode(userRequest.getPassword()));
        userData.setCreate_date(LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toString());
        return userData;
    }

    private UserDataResponse mapDataToResponse(UserData userData) {
        UserDataResponse response = new UserDataResponse();

        response.setUserId(userData.getUserId().toString());
        response.setEmailId(userData.getEmailId());
        response.setMobileNum(userData.getMobileNum());
        response.setName(userData.getName());
        return response;
    }

    // TODO: add update password
    private UserData updatePassword(UserDataRequest userDataRequest) {
        UserData userDataFromDb = StringUtils.isEmpty(userDataRequest.getName())
                ? getUserDataByEmailIdOrMobileNum(userDataRequest.getEmailId(), null) : getUserDataByName(userDataRequest.getName());

        if (checkPasswordsMatch(userDataRequest.getPassword(), userDataFromDb.getPassword())) {
            // Executed only when user entered password and db password match
            userDataFromDb.setPassword(bCryptPasswordEncoder.encode(userDataRequest.getPassword()));
            userDataFromDb.setModified_date(LocalDateTime.now(ZoneId.of("Asia/Kolkata")).toString());
            return userDataFromDb;
        } else {
            // User password does not match
            return null;
        }
    }
}