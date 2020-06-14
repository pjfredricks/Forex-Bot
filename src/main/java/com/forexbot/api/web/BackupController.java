package com.forexbot.api.web;

import com.forexbot.api.service.BackupService;
import com.forexbot.api.web.utils.ResponseWrapper;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.forexbot.api.web.utils.Constants.ERROR;
import static com.forexbot.api.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("api/v1")
public class BackupController {

    private final BackupService backupService;

    public BackupController(BackupService backupService) {
        this.backupService = backupService;
    }

    @GetMapping(path = "/backup")
    public ResponseEntity<ResponseWrapper> backupOrder() {
        if (backupService.runBackup()) {
            return new ResponseEntity<>(new ResponseWrapper(
                    SUCCESS,
                    "Orders and Users backup successful",
                    null),
                    HttpStatus.CREATED);
        }
        return new ResponseEntity<>(new ResponseWrapper(
                ERROR,
                "Backup Failed",
                null),
                HttpStatus.CREATED);
    }
}