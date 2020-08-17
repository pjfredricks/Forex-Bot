package com.forexbot.api.web.customer;

import com.forexbot.api.dao.customer.CustomerRequest;
import com.forexbot.api.dao.customer.CustomerResponse;
import com.forexbot.api.service.CustomerService;
import com.forexbot.api.web.utils.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.forexbot.api.web.utils.Constants.ERROR;
import static com.forexbot.api.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("/api/v1")
public class CustomerController {

    private final CustomerService customerService;
    private final EmailController emailController;

    public CustomerController(CustomerService customerService, EmailController emailController) {
        this.customerService = customerService;
        this.emailController = emailController;
    }

    @PostMapping(path = "/signUp")
    public ResponseEntity<ResponseWrapper> signUpCustomer(@RequestBody CustomerRequest customerRequest) {
        try {
            CustomerResponse response = customerService.signUpCustomer(customerRequest);
            emailController.sendWelcomeEmail(customerRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Customer signed Up successfully",
                    response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Customer details already exist for " + customerRequest.getMobileNum() + " and " + customerRequest.getEmailId(),
                    null), HttpStatus.OK);
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<ResponseWrapper> login(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse customerResponse = null;
        try {
            customerResponse = customerService.login(customerRequest);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Error logging in: " + e.getMessage(),
                    null), HttpStatus.OK);
        }
        if (customerResponse != null) {
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Login success",
                    customerResponse), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                ERROR,
                "Incorrect Credentials or Inactive Customer",
                null), HttpStatus.OK);
    }

    // Not integrated with UI
    @PutMapping(path = "/resetPassword")
    public ResponseEntity<ResponseWrapper> resetCustomerPassword(@RequestBody CustomerRequest resetRequest) {
        try {
            CustomerResponse response = customerService.resetCustomerPassword(resetRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Password updated successfully",
                    response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    e.getMessage(),
                    null), HttpStatus.OK);
        }
    }

    @PutMapping(path = "/updateDetails")
    public ResponseEntity<ResponseWrapper> updateCustomerDetails(@RequestBody CustomerRequest resetRequest) {
        try {
            CustomerResponse response = customerService.updateCustomerDetails(resetRequest);
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Customer details updated successfully",
                    response), HttpStatus.OK);
        } catch (Exception e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    e.getMessage(),
                    null), HttpStatus.OK);
        }
    }
}