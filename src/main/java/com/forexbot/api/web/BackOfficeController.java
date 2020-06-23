package com.forexbot.api.web;

import com.forexbot.api.dao.admin.*;
import com.forexbot.api.service.BackOfficeService;
import com.forexbot.api.service.OrderService;
import com.forexbot.api.service.RatesService;
import com.forexbot.api.service.UserDataService;
import com.forexbot.api.web.utils.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.forexbot.api.web.utils.Constants.ERROR;
import static com.forexbot.api.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("api/v1/admin")
public class BackOfficeController {

    private final OrderService orderService;
    private final UserDataService userDataService;
    private final RatesService ratesService;
    private final BackOfficeService backOfficeService;

    public BackOfficeController(OrderService orderService, UserDataService userDataService, RatesService ratesService, BackOfficeService backOfficeService) {
        this.orderService = orderService;
        this.userDataService = userDataService;
        this.ratesService = ratesService;
        this.backOfficeService = backOfficeService;
    }

    @PostMapping(path = "/login")
    public ResponseEntity<ResponseWrapper> loginVendor(@RequestBody BackOfficeLoginRequest loginRequest) {
        try {
            BackOfficeLoginResponse response = backOfficeService.login(loginRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Login success",
                    response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Incorrect Credentials",
                    null), HttpStatus.OK);
        }
    }

    @PostMapping(path = "/create/admin")
    public ResponseEntity<ResponseWrapper> createAdmin(@RequestBody BackOfficeSignInRequest signInRequest) {
        try {
            BackOfficeUserData responseData = backOfficeService.createAdmin(signInRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Admin created successfully for user: " + responseData.getUserName(),
                    responseData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Admin already registered",
                    null), HttpStatus.OK);
        }
    }

    @PostMapping(path = "/create/vendor")
    public ResponseEntity<ResponseWrapper> createVendor(@RequestBody BackOfficeSignInRequest signInRequest) {

        try {
            BackOfficeUserData responseData = backOfficeService.createVendor(signInRequest);
            //TODO: Send vendor email
//            emailController.sendWelcomeEmail(userDataRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Vendor registered successfully for id: " + responseData.getVendorAgentId(),
                    responseData), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Vendor already registered",
                    null), HttpStatus.OK);
        }
    }

    @GetMapping(path = "/dailyRates/{date}")
    public ResponseEntity<ResponseWrapper> getRatesByDate(@PathVariable String date) {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Rates for " + date + "have been fetched",
                ratesService.getExchangeRates()), HttpStatus.OK);
    }

    @GetMapping(path = "/orders")
    public ResponseEntity<ResponseWrapper> getAllOrders() {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Orders have been fetched",
                orderService.getAllOrders()), HttpStatus.OK);
    }

    @GetMapping(path = "/users")
    public ResponseEntity<ResponseWrapper> getAllUsers() {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Users have been fetched",
                backOfficeService.getAllUsers()), HttpStatus.OK);
    }
}
