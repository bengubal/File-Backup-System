package com.example.model;


import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;

import java.util.ArrayList;
import java.util.List;


@Document(collection = "users")
public class User {

    @Id
    private String userId;
    private String username;
    private String password;
    private List<String> uploadedFiles; 
    private long storageLimit;
    private boolean passwordChangeRequest;
    private String role = "ROLE_USER";    
    private List<String> teams; 
    private List<String> friends; 
    private List<String> sharedFiles; 

    public User(){
        this.teams=new ArrayList<>();
        this.friends=new ArrayList<>();
        this.sharedFiles=new ArrayList<>();
        
    }


    public User(String userId, String username, String password, String role, long storageLimit) {
        this.userId = userId;
        this.username = username;
        this.password = password;
        this.role = role;
        this.storageLimit = storageLimit;
        this.teams=new ArrayList<>();
    }


    // Getter ve Setter metodları
    public String getId() {
        return userId;
    }

    public void setId(String userId) {
        this.userId = userId;
    }

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

    public List<String> getTeams() {
        return teams;
    }

    public void setTeams(List<String> teams) {
        this.teams = teams;
    }
    
    public void addTeam(String teamId){
        this.teams.add(teamId);
    }
    
    public void removeTeam(String teamId){
        this.teams.remove(teamId);
    }

    public List<String> getUploadedFiles() {
        return uploadedFiles;
    }

    public void setUploadedFiles(List<String> uploadedFiles) {
        this.uploadedFiles = uploadedFiles;
    }
    
    public long getStorageLimit() {
        return storageLimit;
    }

    public void setStorageLimit(long storageLimit) {
        this.storageLimit = storageLimit;
    }
    
    public String getRole() {
        return role;
    }
    
    public void setRole(String role) {
        this.role= role;
    }
    
    public List<String> getFriends() {
        return friends;
    }

    // Setter for friends
    public void setFriends(List<String> friends) {
        this.friends = friends;
    }

    // Getter for sharedFiles
    public List<String> getSharedFiles() {
        return sharedFiles;
    }

    // Setter for sharedFiles
    public void setSharedFiles(List<String> sharedFiles) {
        this.sharedFiles = sharedFiles;
    }
}
