package com.example.service;

import org.springframework.stereotype.Service;

@Service
public class LoggingService {

    private final LogService logService;

    public LoggingService(LogService logService) {
        this.logService = logService;
    }


    public void logBackupResult(String username, String action, String status, String details) {
        // Kullanıcı işlem logunu kaydet
        logService.log(username, action, status, details);
    }


    public void logSynchronization(String username, String action, String status, String details) {
        logService.log(username, action, status, details);
    }


    public void logAnomaly(String username, String action, String status, String anomalyMessage) {
        logService.log(username, action, status, anomalyMessage);
    }
}
