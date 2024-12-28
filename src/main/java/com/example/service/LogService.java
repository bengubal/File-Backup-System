package com.example.service;

import com.example.model.LogEntry;
import com.example.model.User;
import com.example.repository.LogEntryRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class LogService {
    private final LogEntryRepository logEntryRepository;
    private final UserRepository userRepository;


     
    public LogService(LogEntryRepository logEntryRepository, UserRepository userRepository){
        this.logEntryRepository = logEntryRepository;
         this.userRepository= userRepository;
    }

    public List<LogEntry> getUserLogs(String userId) {
     
        return logEntryRepository.findByUser_Id(userId);
    }
    public List<LogEntry> getAllLogs(){
        return logEntryRepository.findAll();
    }
    public void saveLog(LogEntry logEntry){
        logEntryRepository.save(logEntry);
    }
       public User getUserByUsername(String username) {
          Optional<User> userOptional = userRepository.findByUsername(username);
           return userOptional.orElse(null);
    }
       public User getUserById(String userId){
            Optional<User> optionalUser = userRepository.findById(userId);
            return optionalUser.orElse(null);
        }

}