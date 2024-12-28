package com.example.controller;

import java.util.Optional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import com.example.model.LoginRequest;
import com.example.model.User;
import com.example.model.UserResponse;
import com.example.repository.UserRepository;
import com.example.service.UserService;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpSession;

@RestController
@RequestMapping("/auth")
public class AuthController {
	
	 @Autowired
	   private PasswordEncoder passwordEncoder;
	@Autowired
	private UserService userService;
	@Autowired
	private UserRepository userRepository;
	
	 @PostMapping("/register")
	    public ResponseEntity<String> registerUser(@RequestBody User user) {
	        userService.registerUser(user.getUsername(), user.getPassword(), user.getRole(), user.getStorageLimit());
	        return ResponseEntity.status(HttpStatus.CREATED).body("User registered successfully");
	    }

	 @PostMapping("/login")
	 public ResponseEntity<?> login(@RequestBody LoginRequest loginRequest, HttpServletRequest request) {
		    System.out.println("Gelen kullanıcı adı: " + loginRequest.getUsername());
		    System.out.println("Gelen şifre: " + loginRequest.getPassword());
		 User user = userService.getUserByUsername(loginRequest.getUsername());

		    if (user != null && passwordEncoder.matches(loginRequest.getPassword(), user.getPassword())) {
		        HttpSession session = request.getSession();

		        if (user.getRole().equals("ROLE_ADMIN")) {
		            session.setAttribute("admin", true);
		            return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), "admin"));
		        } else if (user.getRole().equals("ROLE_USER")) {
		            session.setAttribute("user", true);
		            return ResponseEntity.ok(new UserResponse(user.getId(), user.getUsername(), "user"));
		        }
		    }

		    // Giriş başarısız, kullanıcı adı veya şifre yanlış ya da rol uygun değil
		    return ResponseEntity.status(HttpStatus.UNAUTHORIZED).build();
		}

	     @GetMapping("/admin/check-access")
	     public ResponseEntity<?> checkAdminAccess(HttpSession session) {
	         // Admin olup olmadığını kontrol et
	         if (session.getAttribute("admin") != null) {
	             return ResponseEntity.ok().build();
	         }
	         return ResponseEntity.status(HttpStatus.FORBIDDEN).build();
	     }
	 


	@PostMapping("/update")
	public ResponseEntity<?> updateUser(@PathVariable User user) {

		Optional<User> existingUser = userRepository.findByUsername(user.getUsername());
		User dbUser = existingUser.get();
		dbUser.setUsername(user.getUsername());
	    return ResponseEntity.ok(dbUser);
	}
		
}
