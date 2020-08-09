package com.forexbot.api.web.customer;

import com.forexbot.api.service.CustomerService;
import com.forexbot.api.web.utils.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.forexbot.api.web.utils.Constants.ERROR;
import static com.forexbot.api.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("/api/v1")
public class CustomerBackOfficeController {

    private final CustomerService customerService;

    public CustomerBackOfficeController(CustomerService customerService) {
        this.customerService = customerService;
    }

    @GetMapping(path = "/customer")
    public ResponseEntity<ResponseWrapper> getAllCustomers() {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "All customer details fetched",
                customerService.getAllCustomers()), HttpStatus.OK);
    }

    @DeleteMapping(path = "/customer/{customerId}")
    public ResponseEntity<ResponseWrapper> deleteCustomerById(@PathVariable String customerId) {
        customerService.deleteCustomer(UUID.fromString(customerId));
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Customer deleted successfully",
                null),
                HttpStatus.OK);
    }

    @GetMapping(path = "/customer/{customerId}")
    public ResponseEntity<ResponseWrapper> getCustomerDetailsByCustomerId(@PathVariable String customerId) {
        try {
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Customer Details fetched for customerId: " + customerId,
                    customerService.getCustomerIdDetailsById(UUID.fromString(customerId))),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Invalid CustomerId " + customerId,
                    null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/customer/mobileNum/{mobileNum}")
    public ResponseEntity<ResponseWrapper> getCustomerDetailsByMobileNum(@PathVariable String mobileNum) {
        try {
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Customer Details fetched for mobile number: " + mobileNum,
                    customerService.getCustomerDataByEmailIdOrMobileNum(null, mobileNum)),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Invalid mobile number " + mobileNum,
                    null),
                    HttpStatus.BAD_REQUEST);
        }
    }

    @GetMapping(path = "/customer/emailId/{emailId}")
    public ResponseEntity<ResponseWrapper> getCustomerDetailsByEmailId(@PathVariable String emailId) {
        try {
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Customer Details fetched for emailId: " + emailId,
                    customerService.getCustomerDataByEmailIdOrMobileNum(emailId, null)),
                    HttpStatus.OK);
        } catch (IllegalArgumentException e) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "Invalid emailId " + emailId,
                    null),
                    HttpStatus.BAD_REQUEST);
        }
    }
}