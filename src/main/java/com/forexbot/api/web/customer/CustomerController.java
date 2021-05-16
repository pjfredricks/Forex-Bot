package com.forexbot.api.web.customer;

import com.forexbot.api.dao.customer.CustomerRequest;
import com.forexbot.api.dao.customer.CustomerResponse;
import com.forexbot.api.service.CustomerService;
import com.forexbot.api.web.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.forexbot.api.web.utils.ResponseWrapper.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class CustomerController {

    private final CustomerService customerService;
    private final EmailController emailController;

    @PostMapping(path = "/signUp")
    public ResponseEntity<ResponseWrapper> signUpCustomer(@RequestBody CustomerRequest customerRequest) {
        try {
            CustomerResponse response = customerService.signUpCustomer(customerRequest);
            emailController.sendWelcomeEmail(customerRequest);
            return ResponseEntity.ok(
                    buildSuccessResponse("Customer signed Up successfully", response)
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(
                    buildErrorResponse("Customer details already exist for " + customerRequest.getMobileNum() + " and " + customerRequest.getEmailId(), null)
            );
        }
    }

    @PostMapping(path = "/login")
    public ResponseEntity<ResponseWrapper> login(@RequestBody CustomerRequest customerRequest) {
        CustomerResponse customerResponse = null;
        try {
            customerResponse = customerService.login(customerRequest);
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(
                    buildErrorResponse("Error logging in: " + e.getMessage(), null)
            );
        }
        if (customerResponse != null) {
            return ResponseEntity.ok(
                    buildSuccessResponse("Login success", customerResponse)
            );
        }
        return ResponseEntity.ok(
                buildErrorResponse("Incorrect Credentials or Inactive Customer", null)
        );
    }

    // Not integrated with UI
    @PutMapping(path = "/resetPassword")
    public ResponseEntity<ResponseWrapper> resetCustomerPassword(@RequestBody CustomerRequest resetRequest) {
        try {
            CustomerResponse response = customerService.resetCustomerPassword(resetRequest);
            return ResponseEntity.ok(
                    buildSuccessResponse("Password updated successfully", response)
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    buildErrorResponse(e.getMessage(), null)
            );
        }
    }

    @PutMapping(path = "/updateDetails")
    public ResponseEntity<ResponseWrapper> updateCustomerDetails(@RequestBody CustomerRequest resetRequest) {
        try {
            CustomerResponse response = customerService.updateCustomerDetails(resetRequest);
            return ResponseEntity.ok(
                    buildSuccessResponse("Customer details updated successfully", response)
            );
        } catch (Exception e) {
            return ResponseEntity.ok(
                    buildErrorResponse(e.getMessage(), null)
            );
        }
    }
}
