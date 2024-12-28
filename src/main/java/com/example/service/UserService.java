package com.example.service;

import com.example.model.User;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final UserRepository userRepository;
    private final PasswordEncoder passwordEncoder;
     @Autowired
    public UserService(UserRepository userRepository,PasswordEncoder passwordEncoder) {
        this.userRepository = userRepository;
        this.passwordEncoder= passwordEncoder;
    }
    public User registerUser(String username, String password, String role, long storageLimit) {
        Optional<User> existingUser = userRepository.findByUsername(username);
        if (existingUser.isPresent()) {
            throw new IllegalArgumentException("Username already exists!");
        }
        User newUser = new User();
        newUser.setUsername(username);
        newUser.setPassword(passwordEncoder.encode(password)); 
        newUser.setRole(role);
        newUser.setStorageLimit(storageLimit);
        return userRepository.save(newUser);
    }
    
    
    public User registerAdmin(String username, String password,long storageLimit) {
        Optional<User> existingUser = userRepository.findByUsername(username);
         if (existingUser.isPresent()) {
             throw new IllegalArgumentException("Username already exists!");
         }
        User newAdmin = new User();
        newAdmin.setUsername(username);
        newAdmin.setPassword(passwordEncoder.encode(password)); 
        newAdmin.setRole("ROLE_ADMIN");
        newAdmin.setStorageLimit(storageLimit);
        return userRepository.save(newAdmin);
   }
    

     public User loginUser(String username, String password) {
            Optional<User> user = userRepository.findByUsername(username);
            if (user.isPresent()) {
                 if (passwordEncoder.matches(password, user.get().getPassword())) { 
                    return user.get();
                } else {
                    throw new IllegalArgumentException("Invalid password!");
                }
            } else {
                throw new IllegalArgumentException("User not found!");
            }
        }
     
      public User getUserByUsername(String username) {
           return userRepository.findByUsername(username)
                   .orElseThrow(() -> new IllegalArgumentException("User not found!"));
      }
      
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }
    
      public User getUserById(String userId){
          Optional<User> optionalUser = userRepository.findById(userId);
          return optionalUser.orElse(null);
      }
      
    public User updateUserStorageLimit(String userId, long storageLimit) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setStorageLimit(storageLimit);
            return userRepository.save(user);
        }
        return null;
    }
    

    public User updatePassword(String userId, String plainPassword) {
        Optional<User> optionalUser = userRepository.findById(userId);
        if(optionalUser.isPresent()){
            User user = optionalUser.get();
            user.setPassword(plainPassword); // Hashleme işlemini kaldırdık
            return userRepository.save(user);
        }
        return null;
    }
    
    
    public void deleteUser(String id) {
        userRepository.deleteById(id);
 
    }
    
    public String addFriendByUsername(String username, String friendUsername) {
        User user = userRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı: " + username));
        
        User friend = userRepository.findByUsername(friendUsername)
                .orElseThrow(() -> new RuntimeException("Arkadaş bulunamadı: " + friendUsername));
        
        if (!user.getFriends().contains(friend.getId())) {
            user.getFriends().add(friend.getId());
            userRepository.save(user);
        }

        if (!friend.getFriends().contains(user.getId())) {
            friend.getFriends().add(user.getId());
            userRepository.save(friend);
        }

        return "Arkadaş başarıyla eklendi!";
    }

}