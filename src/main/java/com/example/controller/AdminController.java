package com.example.controller;

import com.example.model.LogEntry;
import com.example.model.PasswordChangeRequest;
import com.example.model.User;
import com.example.service.LogService;
import com.example.service.PasswordChangeRequestService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.List;

@RestController
@RequestMapping("/admin")
public class AdminController {
    @Autowired
    private final UserService userService;
    private final LogService logService;
    private final PasswordChangeRequestService passwordChangeRequestService;

    public AdminController(UserService userService, LogService logService, PasswordChangeRequestService passwordChangeRequestService) {
        this.userService = userService;
        this.logService = logService;
        this.passwordChangeRequestService = passwordChangeRequestService;
    }
    
    @PostMapping("/register-admin")
    public ResponseEntity<?> registerAdmin(@RequestParam String username, @RequestParam String password,@RequestParam long storageLimit){
      User newAdmin = userService.registerAdmin(username,password,storageLimit);
        return ResponseEntity.ok(newAdmin);
    }

    @GetMapping("/users/{adminId}")
    public ResponseEntity<?> getAllUsers(@PathVariable String adminId) {
    	User admin = userService.getUserById(adminId);
    	try {
        
        if(admin != null && admin.getRole().equals("ROLE_ADMIN")){
            List<User> users = userService.getAllUsers();
            return ResponseEntity.ok(users);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have admin access!");
    	}
    	catch(Exception ex)
    	{
    		return ResponseEntity.status(HttpStatus.ACCEPTED).body(admin.toString());
    	}
    }

    @PutMapping("/users/{adminId}/storage-limit/{id}")
    public ResponseEntity<?> updateUserStorageLimit(@PathVariable String adminId,@PathVariable String id, @RequestParam long storageLimit) {
        User admin = userService.getUserById(adminId);
        if(admin != null && admin.getRole().equals("ROLE_ADMIN")){
            User updatedUser = userService.updateUserStorageLimit(id, storageLimit);
            return ResponseEntity.ok(updatedUser);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have admin access!");
    }
    

    @GetMapping("/users/{adminId}/logs/{id}")
    public ResponseEntity<?> getUserLogs(@PathVariable String adminId, @PathVariable String id) {
        User admin = userService.getUserById(adminId);
        if(admin != null && admin.getRole().equals("ROLE_ADMIN")){
            List<LogEntry> logs = logService.getUserLogs(id);
            return ResponseEntity.ok(logs);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have admin access!");
    }
    

    @GetMapping("/logs/{adminId}")
    public ResponseEntity<?> getAllLogs(@PathVariable String adminId){
        User admin = userService.getUserById(adminId);
        if(admin != null && admin.getRole().equals("ROLE_ADMIN")){
            List<LogEntry> allLogs = logService.getAllLogs();
            return ResponseEntity.ok(allLogs);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have admin access!");
    }

    
    @PutMapping("/users/{adminId}/password/{userId}")
    public ResponseEntity<?> updateUserPassword(@PathVariable String adminId, @PathVariable String userId, @RequestParam String newPassword){
        User admin = userService.getUserById(adminId);
        if (admin != null && admin.getRole().equals("ROLE_ADMIN")){
            String hashedPassword = passwordEncoder.encode(newPassword);
            User user = userService.updatePassword(userId,hashedPassword);
            if (user != null){
                 return ResponseEntity.ok("Password changed succesfully!");
            }
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("User not found!");

        }
        return  ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have admin access!");
    }
     
    
    @PostMapping("/users/{adminId}/register/{role}")
     public ResponseEntity<?> registerUser(@PathVariable String adminId, @RequestParam String username, @RequestParam String password, @PathVariable String role, @RequestParam long storageLimit){
          User admin = userService.getUserById(adminId);
        if (admin != null && admin.getRole().equals("ROLE_ADMIN")){
            User registeredUser = userService.registerUser(username,password,role,storageLimit);
           return ResponseEntity.ok(registeredUser);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have admin access!");
     }
    
    @GetMapping("/password-change-requests/{adminId}")
    public ResponseEntity<?> getPendingPasswordChangeRequests(@PathVariable String adminId) {
    	
    	User admin = userService.getUserById(adminId);
        if (admin != null && admin.getRole().equals("ROLE_ADMIN")) {
        List<PasswordChangeRequest> requests = passwordChangeRequestService.getPasswordChangeRequest();
        return new ResponseEntity<>(requests, HttpStatus.OK);
        }
        
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have admin access!");
    }



    
    @PostMapping("/password-change-requests/{adminId}/{requestId}/approve")
    public ResponseEntity<String> approvePasswordChangeRequest(@PathVariable String adminId, @PathVariable String requestId){
    	User admin = userService.getUserById(adminId);
        if (admin != null && admin.getRole().equals("ROLE_ADMIN")){
        passwordChangeRequestService.approvePasswordChangeRequest(requestId);
        return new ResponseEntity<>("Password change request approved successfully",HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have admin access");
    }
    

    
    @PostMapping("/password-change-requests/{adminId}/{requestId}/reject")
    public ResponseEntity<String> rejectPasswordChangeRequest(@PathVariable String adminId, @PathVariable String requestId){
    	User admin = userService.getUserById(adminId);
        if (admin != null && admin.getRole().equals("ROLE_ADMIN")){
        passwordChangeRequestService.rejectPasswordChangeRequest(requestId);
        return new ResponseEntity<>("Password change request rejected successfully",HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have admin access!");
    }
    
    @DeleteMapping("/users/{adminId}/delete/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable String adminId, @PathVariable String id) {
         User admin = userService.getUserById(adminId);
        if(admin != null && admin.getRole().equals("ROLE_ADMIN")){
            userService.deleteUser(id);
            return new ResponseEntity<>("User deleted successfully", HttpStatus.OK);
        }
        return ResponseEntity.status(HttpStatus.FORBIDDEN).body("You do not have admin access!");
    }
    
   @Autowired
    private PasswordEncoder passwordEncoder;
}