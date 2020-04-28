package com.example.demo.web;

import com.example.demo.repository.dao.ResponseWrapper;
import com.example.demo.service.BackupService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import static com.example.demo.web.utils.Constants.ERROR;
import static com.example.demo.web.utils.Constants.SUCCESS;

@RestController
@RequestMapping("api/v1")
public class BackupController {

    private BackupService backupService;

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