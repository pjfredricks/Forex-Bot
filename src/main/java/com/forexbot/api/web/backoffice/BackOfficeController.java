package com.forexbot.api.web.backoffice;

import com.forexbot.api.dao.backoffice.userdata.*;
import com.forexbot.api.service.BackOfficeService;
import com.forexbot.api.web.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.forexbot.api.web.utils.ResponseWrapper.*;

@Slf4j
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class BackOfficeController {

    private final BackOfficeService backOfficeService;

    @PostMapping(path = "/backOfficeLogin")
    public ResponseEntity<ResponseWrapper> loginVendor(@RequestBody BackOfficeLoginRequest loginRequest) {
        try {
            BackOfficeLoginResponse response = backOfficeService.login(loginRequest);
            return ResponseEntity.ok(
                    buildSuccessResponse("Login success", response)
            );
        } catch (Exception e) {
            String message = "Incorrect Credentials, Error:" + e.getMessage();
            log.error(message);
            return ResponseEntity.ok(
                    buildErrorResponse(message, null)
            );
        }
    }

    @PostMapping(path = "/create/admin")
    public ResponseEntity<ResponseWrapper> createAdmin(@RequestBody BackOfficeSignInRequest signInRequest) {
        try {
            BackOfficeUserData responseData = backOfficeService.createAdmin(signInRequest);
            return ResponseEntity.ok(
                    buildSuccessResponse("Admin created successfully for user: " + responseData.getUserName(),
                            responseData)
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(
                    buildErrorResponse("Admin already registered, Error: " + e.getMessage(), null)
            );
        }
    }

    @PostMapping(path = "/create/vendor")
    public ResponseEntity<ResponseWrapper> createVendor(@RequestBody BackOfficeSignInRequest signInRequest) {

        try {
            BackOfficeUserData responseData = backOfficeService.createVendor(signInRequest);
            return ResponseEntity.ok(
                    buildSuccessResponse("Vendor registered successfully for id: " + responseData.getVendorAgentId(),
                            responseData)
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(
                    buildErrorResponse("Vendor already registered, Error:" + e.getMessage(), null)
            );
        }
    }

    @PutMapping(path = "/update/{userId}")
    public ResponseEntity<ResponseWrapper> update(@RequestBody BackOfficeSignInRequest signInRequest,
                                                  @PathVariable String userId) {
        try {
            BackOfficeUserResponse responseData = backOfficeService.update(signInRequest, userId);
            return ResponseEntity.ok(
                    buildSuccessResponse("Details updated successfully for user: " + responseData.getUserName(),
                            responseData)
            );
        } catch (Exception e) {
            log.error(e.getMessage());
            return ResponseEntity.ok(
                    buildErrorResponse("Error updating details, Error:" + e.getMessage(), null)
            );
        }
    }
}
