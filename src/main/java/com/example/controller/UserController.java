package com.example.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.example.model.User;
import com.example.repository.UserRepository;

import com.example.service.PasswordChangeRequestService;
import com.example.service.UserService;

@RestController
@RequestMapping("/fms/user")
public class UserController {
	
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	@Autowired
	private PasswordChangeRequestService passwordChangeRequestService;


	
	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserByUsername(@PathVariable String userId) {

	    User dbUser = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("User not found!"));


	    return ResponseEntity.ok(dbUser);
	}

	
    @PostMapping("/{userId}/password-change-request")
    public ResponseEntity<String> requestPasswordChange(@PathVariable String userId, @RequestParam String newPassword) {
        passwordChangeRequestService.savePasswordChangeRequest(userId, newPassword);
        System.out.println("asdfghjm");
        return new ResponseEntity<>("Password change request sent successfully", HttpStatus.OK);
    }
    


    @PostMapping("/addFriend")
    public ResponseEntity<String> addFriendById(@RequestParam String userId, @RequestParam String friendId) {
        String message = userService.addFriendById(userId, friendId);
        return ResponseEntity.ok(message);
    }
    
    @GetMapping("/friends/{userId}")
    public ResponseEntity<List<String>> getUserFriends(@PathVariable String userId) {
        try {
            // Kullanıcı ID'sine göre arkadaşları al
            List<String> friendsUsernames = userService.getUserFriends(userId);
            return ResponseEntity.ok(friendsUsernames); // Arkadaşları döndür
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }



}
