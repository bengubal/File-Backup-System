package com.example.model;


public class UserResponse {
    private String id;
    private String username;
    private String role; // Rol bilgisi

    // Constructor
    public UserResponse(String id, String username, String role) {
        this.id = id;
        this.username = username;
        this.role = role;
    }

    // Getter ve Setter metodları
    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getRole() {
        return role;
    }

    public void setRole(String role) {
        this.role = role;
    }
}
