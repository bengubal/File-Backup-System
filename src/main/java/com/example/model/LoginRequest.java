package com.example.model;


public class LoginRequest {

    private String username;
    private String password;
    

    // Varsayılan constructor
    public LoginRequest() {
    }

    // Parametreli constructor
    public LoginRequest(String username, String password) {
        this.username = username;
        this.password = password;
    }

    // Getter ve Setter metodları
    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    // toString, equals, hashCode gibi metotlar gerekirse eklenebilir
}
