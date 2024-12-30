package com.example.service;

import com.example.model.LogEntry;
import com.example.repository.LogEntryRepository;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.io.FileWriter;
import java.io.IOException;
import java.io.PrintWriter;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class LogService {

    private final LogEntryRepository logEntryRepository;

    @Value("${log.file.path}")
    private String logFilePath;

    public LogService(LogEntryRepository logEntryRepository) {
        this.logEntryRepository = logEntryRepository;
    }


    public void log(String username, String action, String status, String details) {
        // Log nesnesi oluştur
        LogEntry logEntry = new LogEntry();
        logEntry.setUsername(username);
        logEntry.setAction(action);
        logEntry.setStatus(status);
        logEntry.setDetails(details);
        logEntry.setTimestamp(LocalDateTime.now());

        // MongoDB'ye kaydet
        saveLogToDatabase(logEntry);

        // Dosyaya kaydet
        saveLogToFile(username, action, status, details);
    }

 
    private void saveLogToDatabase(LogEntry logEntry) {
        logEntryRepository.save(logEntry);
    }


    private void saveLogToFile(String username, String action, String status, String details) {
        try (FileWriter fw = new FileWriter(logFilePath, true);  // true: Append mode
             PrintWriter pw = new PrintWriter(fw)) {

            // Log mesajını oluştur
            String logMessage = String.format("%s - User: %s, Action: %s, Status: %s, Details: %s",
                    LocalDateTime.now(), username, action, status, details);

            // Log mesajını dosyaya yaz
            pw.println(logMessage);

        } catch (IOException e) {
            e.printStackTrace();
        }
    }
    
    public List<LogEntry> getAllLogs() {
        return logEntryRepository.findAll();
    }
}
