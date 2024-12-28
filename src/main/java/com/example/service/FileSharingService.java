package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.User;
import com.example.repository.UserRepository;

@Service
public class FileSharingService {
    @Autowired
    private UserRepository userRepository;

    public String shareFile(String userId, String fileName) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
        if (!user.getSharedFiles().contains(fileName)) {
            user.getSharedFiles().add(fileName);
            userRepository.save(user);
        }
        return "Dosya paylaşıldı!";
    }
}

