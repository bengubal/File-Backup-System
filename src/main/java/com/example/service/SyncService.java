package com.example.service;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;

@Service
public class SyncService {
    @Value("${backup.source.directory}")
    private String sourceDirectory;

    @Value("${backup.target.directory}")
    private String targetDirectory;
    
    @Autowired
    private final LoggingService loggingService;

    public SyncService(LoggingService loggingService) {
        this.loggingService = loggingService;
    }
    
    @Scheduled(fixedRate = 100000) 
    public void synchronizeDirectories() {
        try (Stream<Path> files = Files.list(Paths.get(sourceDirectory))) {
            files.forEach(file -> {
                try {
                    Path targetPath = Paths.get(targetDirectory, file.getFileName().toString());
                    Files.copy(file, targetPath, StandardCopyOption.REPLACE_EXISTING);
                    System.out.println("Dosya senkronize edildi: " + targetPath);

                  //  loggingService.logBackupResult("-1", "Synced", "200", "Saved");
                } catch (IOException e) {
                    e.printStackTrace();
                }
            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
}

