package com.example.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

@Document(collection = "password_change_requests")
public class PasswordChangeRequest {
    @Id
    private String id;
    private String userId;
    private String newPassword;
    private String status;
    //Varsayılan Constructor
     public PasswordChangeRequest(){

     }


    public PasswordChangeRequest(String userId, String newPassword, String status) {
        this.userId = userId;
        this.newPassword = newPassword;
        this.status = status;
    }

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

     public String getNewPassword() {
        return newPassword;
    }

    public void setNewPassword(String newPassword) {
        this.newPassword = newPassword;
    }


    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }
}