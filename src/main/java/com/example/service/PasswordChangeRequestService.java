package com.example.service;

import com.example.model.PasswordChangeRequest;
import com.example.repository.PasswordChangeRequestRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class PasswordChangeRequestService {
    private final PasswordChangeRequestRepository passwordChangeRequestRepository;
    private final UserService userService;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public PasswordChangeRequestService(PasswordChangeRequestRepository passwordChangeRequestRepository,UserService userService,PasswordEncoder passwordEncoder){
        this.passwordChangeRequestRepository = passwordChangeRequestRepository;
        this.userService = userService;
        this.passwordEncoder = passwordEncoder;
    }

    public void savePasswordChangeRequest(String userId, String newPassword){
        if (passwordEncoder == null) {
                System.out.println("Error: Password encoder is null.");
                return;
        }
        if (newPassword == null || newPassword.trim().isEmpty()) {
               System.out.println("Error: New password cannot be null or empty.");
               return;
        }
      String hashedPassword = passwordEncoder.encode(newPassword);
       System.out.println("service");
       PasswordChangeRequest passwordChangeRequest = new PasswordChangeRequest(userId,hashedPassword,"pending");
       if (passwordChangeRequestRepository != null) {
             passwordChangeRequestRepository.save(passwordChangeRequest);
           } else {
               System.out.println("Error: passwordChangeRequestRepository is null.");
               }
    }
    
    public List<PasswordChangeRequest> getPasswordChangeRequest(){
         return passwordChangeRequestRepository.findByStatus("pending");
    }
    
    
    public void approvePasswordChangeRequest(String id){
        PasswordChangeRequest passwordChangeRequest = passwordChangeRequestRepository.findById(id).orElse(null);
        if (passwordChangeRequest != null){
            passwordChangeRequest.setStatus("approved");
            userService.updatePassword(passwordChangeRequest.getUserId(),passwordChangeRequest.getNewPassword());
            passwordChangeRequestRepository.save(passwordChangeRequest);
        }
    }
    
    public void rejectPasswordChangeRequest(String id){
         PasswordChangeRequest passwordChangeRequest = passwordChangeRequestRepository.findById(id).orElse(null);
        if (passwordChangeRequest != null){
            passwordChangeRequest.setStatus("rejected");
            passwordChangeRequestRepository.save(passwordChangeRequest);
        }
    }
}