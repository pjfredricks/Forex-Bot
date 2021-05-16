package com.forexbot.api.web.backup;

import com.forexbot.api.service.BackupService;
import com.forexbot.api.web.utils.ResponseWrapper;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import static com.forexbot.api.web.utils.ResponseWrapper.buildErrorResponse;
import static com.forexbot.api.web.utils.ResponseWrapper.buildSuccessResponse;

@Slf4j
@RestController
@RequestMapping("api/v1")
@RequiredArgsConstructor
public class BackupController {

    private final BackupService backupService;

    @GetMapping(path = "/backup")
    public ResponseEntity<ResponseWrapper> backupOrder() {
        if (backupService.runBackup()) {
            return new ResponseEntity<>(
                    buildSuccessResponse("Orders and Customers backup successful", null),
                    HttpStatus.CREATED);
        }
        return new ResponseEntity<>(
                buildErrorResponse("Backup Failed", null),
                HttpStatus.CREATED);
    }
}
