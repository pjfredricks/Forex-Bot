package com.forexbot.api.web;

import com.forexbot.api.dao.admin.AdminRequest;
import com.forexbot.api.dao.admin.VendorRequest;
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
    private final BackOfficeService adminService;

    public BackOfficeController(OrderService orderService, UserDataService userDataService, RatesService ratesService, BackOfficeService adminService) {
        this.orderService = orderService;
        this.userDataService = userDataService;
        this.ratesService = ratesService;
        this.adminService = adminService;
    }

    @PostMapping(path = "/login/admin")
    public ResponseEntity<ResponseWrapper> loginAdmin(@RequestBody AdminRequest adminRequest) {
        try {
            adminService.loginAdmin(adminRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Login success",
                    null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Incorrect Credentials",
                    null), HttpStatus.OK);
        }
    }

    @PostMapping(path = "/login/vendor")
    public ResponseEntity<ResponseWrapper> loginVendor(@RequestBody VendorRequest vendorRequest) {
        try {
            adminService.loginVendor(vendorRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Login success",
                    null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Incorrect Credentials",
                    null), HttpStatus.OK);
        }
    }

    @PostMapping(path = "/create/admin")
    public ResponseEntity<ResponseWrapper> createAdmin(@RequestBody AdminRequest adminRequest) {
        try {
            adminService.createAdmin(adminRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Admin created successfully",
                    null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Admin already registered with details",
                    null), HttpStatus.OK);
        }
    }

    @PostMapping(path = "/create/vendor")
    public ResponseEntity<ResponseWrapper> createVendor(@RequestBody VendorRequest vendorRequest) {

        try {
            adminService.createVendor(vendorRequest);
            //TODO: Send vendor email
//            emailController.sendWelcomeEmail(userDataRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "User signed Up successfully",
                    null), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Vendor already registered",
                    null), HttpStatus.OK);
        }
    }

    @GetMapping(path = "/dailyRates")
    public ResponseEntity<ResponseWrapper> getDailyRates() {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Rates have been fetched",
                ratesService.getExchangeRates()), HttpStatus.OK);
    }

    @GetMapping(path = "/orders")
    public ResponseEntity<ResponseWrapper> getAllOrders() {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Rates have been fetched",
                orderService.getAllOrders()), HttpStatus.OK);
    }
}
