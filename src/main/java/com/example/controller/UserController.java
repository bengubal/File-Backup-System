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
import com.example.service.FileSharingService;
import com.example.service.FileViewingService;
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
    @Autowired
    private FileSharingService fileSharingService;
    @Autowired
    private FileViewingService fileViewingService;
	
	@GetMapping("/{userId}")
	public ResponseEntity<?> getUserByUsername(@PathVariable String userId) {
	    // Veritabanında kullanıcıyı bul
	    User dbUser = userRepository.findById(userId)
	            .orElseThrow(() -> new IllegalArgumentException("User not found!"));

	    // Kullanıcı bilgilerini döndür
	    return ResponseEntity.ok(dbUser);
	}

	
    @PostMapping("/{userId}/password-change-request")
    public ResponseEntity<String> requestPasswordChange(@PathVariable String userId, @RequestParam String newPassword) {
        passwordChangeRequestService.savePasswordChangeRequest(userId, newPassword);
        System.out.println("asdfghjm");
        return new ResponseEntity<>("Password change request sent successfully", HttpStatus.OK);
    }
    


    @PostMapping("/addFriend")
    public ResponseEntity<String> addFriendByUsername(@RequestParam String username, @RequestParam String friendUsername) {
        String message = userService.addFriendByUsername(username, friendUsername);
        return ResponseEntity.ok(message);
    }
	
}
