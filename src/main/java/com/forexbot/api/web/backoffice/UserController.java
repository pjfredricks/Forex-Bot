package com.forexbot.api.web.backoffice;

import com.forexbot.api.dao.backoffice.userdata.BackOfficeUserResponse;
import com.forexbot.api.service.BackOfficeService;
import com.forexbot.api.web.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.forexbot.api.web.utils.ResponseWrapper.buildErrorResponse;
import static com.forexbot.api.web.utils.ResponseWrapper.buildSuccessResponse;

@Slf4j
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class UserController {

    private final BackOfficeService backOfficeService;

    @GetMapping(path = "/users")
    public ResponseEntity<ResponseWrapper> getAllUsers() {
        return ResponseEntity.ok(
                buildSuccessResponse("Users have been fetched", backOfficeService.getAllUsers())
        );
    }

    @GetMapping(path = "/users/{userId}")
    public ResponseEntity<ResponseWrapper> getUserByUserId(@PathVariable String userId) {
        BackOfficeUserResponse response = backOfficeService.getUserByUserId(userId);
        if (ObjectUtils.isEmpty(response)) {
            return ResponseEntity.ok(
                    buildErrorResponse("No Users exist for Id: " + userId, response)
            );
        }
        return ResponseEntity.ok(
                buildSuccessResponse("Users have been fetched", response)
        );
    }

    @DeleteMapping(path = "/user/{userId}")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable String userId,
                                                      @RequestParam String deletedBy) {
        backOfficeService.deleteUser(userId, deletedBy);
        return ResponseEntity.ok(
                buildSuccessResponse("User has been deleted", null)
        );
    }
}
