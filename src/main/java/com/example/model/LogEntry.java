package com.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.time.LocalDateTime;

@Document(collection = "logs")
public class LogEntry {
    @Id
    private String id;
    private LocalDateTime timestamp;
    private String type;
    private String message;
    private User user;
    private String details;
    // Diğer alanlar ve getter/setter'lar...


    public LogEntry(LocalDateTime timestamp, String type, String message, User user, String details) {
        this.timestamp = timestamp;
        this.type = type;
        this.message = message;
        this.user = user;
        this.details = details;
    }
   public LogEntry(){

   }
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public LocalDateTime getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(LocalDateTime timestamp) {
        this.timestamp = timestamp;
    }

    public String getType() {
        return type;
    }

    public void setType(String type) {
        this.type = type;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public User getUser() {
        return user;
    }

    public void setUser(User user) {
        this.user = user;
    }

    public String getDetails() {
        return details;
    }

    public void setDetails(String details) {
        this.details = details;
    }
}