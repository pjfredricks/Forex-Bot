package com.forexbot.api.web.customer;

import com.forexbot.api.service.CustomerService;
import com.forexbot.api.web.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

import static com.forexbot.api.web.utils.ResponseWrapper.buildErrorResponse;
import static com.forexbot.api.web.utils.ResponseWrapper.buildSuccessResponse;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CustomerBackOfficeController {

    private final CustomerService customerService;

    @GetMapping(path = "/customer")
    public ResponseEntity<ResponseWrapper> getAllCustomers() {
        return ResponseEntity.ok(
                buildSuccessResponse("All customer details fetched", customerService.getAllCustomers())
        );
    }

    @DeleteMapping(path = "/customer/{customerId}")
    public ResponseEntity<ResponseWrapper> deleteCustomerById(@PathVariable String customerId,
                                                              @RequestParam String deletedBy) {
        customerService.deleteCustomer(UUID.fromString(customerId), deletedBy);
        return ResponseEntity.ok(
                buildSuccessResponse("Customer deleted successfully", null)
        );
    }

    @GetMapping(path = "/customer/{customerId}")
    public ResponseEntity<ResponseWrapper> getCustomerDetailsByCustomerId(@PathVariable String customerId) {
        try {
            return ResponseEntity.ok(
                    buildSuccessResponse("Customer Details fetched for customerId: " + customerId,
                            customerService.getCustomerIdDetailsById(UUID.fromString(customerId)))
            );
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(buildErrorResponse("Invalid CustomerId " + customerId, null));
        }
    }

    @GetMapping(path = "/customer/mobileNum/{mobileNum}")
    public ResponseEntity<ResponseWrapper> getCustomerDetailsByMobileNum(@PathVariable String mobileNum) {
        try {
            return ResponseEntity.ok(
                    buildSuccessResponse("Customer Details fetched for mobile number: " + mobileNum,
                            customerService.getCustomerDataByEmailIdOrMobileNum(null, mobileNum))
            );
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(buildErrorResponse("Invalid mobile number " + mobileNum, null));
        }
    }

    @GetMapping(path = "/customer/emailId/{emailId}")
    public ResponseEntity<ResponseWrapper> getCustomerDetailsByEmailId(@PathVariable String emailId) {
        try {
            return ResponseEntity.ok(
                    buildSuccessResponse("Customer Details fetched for emailId: " + emailId,
                            customerService.getCustomerDataByEmailIdOrMobileNum(emailId, null))
            );
        } catch (IllegalArgumentException e) {
            log.error(e.getMessage());
            return ResponseEntity
                    .badRequest()
                    .body(buildErrorResponse("Invalid emailId " + emailId, null));
        }
    }
}
