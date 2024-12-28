package com.example.service;

import java.util.ArrayList;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.model.User;
import com.example.repository.UserRepository;

@Service
public class FileViewingService {
    @Autowired
    private UserRepository userRepository;

    public List<String> getFriendsSharedFiles(String userId) {
        User user = userRepository.findById(userId).orElseThrow(() -> new RuntimeException("Kullanıcı bulunamadı!"));
        List<String> friends = user.getFriends();

        List<String> sharedFiles = new ArrayList<>();
        for (String friendId : friends) {
            User friend = userRepository.findById(friendId).orElse(null);
            if (friend != null) {
                sharedFiles.addAll(friend.getSharedFiles());
            }
        }
        return sharedFiles;
    }
}

