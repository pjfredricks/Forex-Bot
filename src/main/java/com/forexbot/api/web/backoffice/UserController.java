package com.forexbot.api.web.backoffice;

import com.forexbot.api.dao.backoffice.userdata.BackOfficeUserResponse;
import com.forexbot.api.service.BackOfficeService;
import com.forexbot.api.web.utils.ResponseWrapper;
import org.apache.commons.lang3.ObjectUtils;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.forexbot.api.web.utils.Constants.ERROR;
import static com.forexbot.api.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("api/v1")
public class UserController {

    private final BackOfficeService backOfficeService;

    public UserController(BackOfficeService backOfficeService) {
        this.backOfficeService = backOfficeService;
    }

    @GetMapping(path = "/users")
    public ResponseEntity<ResponseWrapper> getAllUsers() {
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Users have been fetched",
                backOfficeService.getAllUsers()), HttpStatus.OK);
    }

    @GetMapping(path = "/users/{userId}")
    public ResponseEntity<ResponseWrapper> getUserByUserId(@PathVariable String userId) {
        BackOfficeUserResponse response = backOfficeService.getUserByUserId(userId);
        if (ObjectUtils.isEmpty(response)) {
            return new ResponseEntity<>(new ResponseWrapper(
                    ERROR,
                    "No Users exist for Id: " + userId,
                    response), HttpStatus.OK);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "Users have been fetched",
                response), HttpStatus.OK);
    }

    @DeleteMapping(path = "/user/{userId}")
    public ResponseEntity<ResponseWrapper> deleteUser(@PathVariable String userId,
                                                      @RequestParam String deletedBy) {
        backOfficeService.deleteUser(userId, deletedBy);
        return new ResponseEntity<>(new ResponseWrapper(
                SUCCESS,
                "User has been deleted",
                null), HttpStatus.OK);
    }
}
