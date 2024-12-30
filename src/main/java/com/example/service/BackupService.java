package com.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

import org.apache.commons.logging.Log;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class BackupService {

    @Value("${backup.target.directory}")
    private String backupDirectory;

    private final LoggingService loggingService;

    public BackupService(LoggingService loggingService) {
        this.loggingService = loggingService;
    }

    public void backupFile(String filePath, String userId) {
        Thread backupThread = new Thread(() -> {
            try {
                // Source and target paths
                Path sourcePath = Paths.get(filePath);
                Path targetPath = Paths.get(backupDirectory, sourcePath.getFileName().toString());

                // Perform the backup
                Files.copy(sourcePath, targetPath, StandardCopyOption.REPLACE_EXISTING);

                // Log the successful backup
                loggingService.logBackupResult(
                    userId,
                    "BACKUP",
                    "SUCCESS",
                    String.format("File %s backed up to %s", sourcePath.getFileName(), targetPath)
                );
                loggingService.logBackupResult(userId, "Backup Saved", "200", "Saved");
                System.out.println("File backed up successfully: " + targetPath);
            } catch (IOException e) {
                // Log the failure
                loggingService.logBackupResult(
                    userId,
                    "BACKUP",
                    "FAILURE",
                    String.format("Failed to back up file %s: %s", filePath, e.getMessage())
                );

                e.printStackTrace();
            }
        });

        backupThread.start();
    }
}
