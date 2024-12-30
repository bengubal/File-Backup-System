package com.example.service;

import com.example.model.Share;
import com.example.model.User;
import com.example.repository.ShareRepository;
import com.example.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class ShareService {

    private final ShareRepository shareRepository;
    private final UserRepository userRepository;

     @Autowired
    public ShareService(ShareRepository shareRepository, UserRepository userRepository) {
        this.shareRepository = shareRepository;
        this.userRepository = userRepository;
    }

    public void shareFile(String fileName, String senderId, String receiverId) {
       Share share = new Share(fileName, senderId, receiverId);
        shareRepository.save(share);

        User receiver = userRepository.findById(receiverId).orElse(null);
        User sender = userRepository.findById(senderId).orElse(null);
        if(receiver!= null && sender!=null) {
            receiver.getSharedFiles().add(fileName);
            userRepository.save(receiver);
           }


    }


    public List<Share> getSharedFiles(String userId) {
      return shareRepository.findByReceiverId(userId);

    }

    public List<Share> getSharedFilesForUser(String userId) {
        User user = userRepository.findById(userId).orElse(null);
        if(user == null){
            throw new RuntimeException("User not found with id: " + userId);
        }
        List<Share> allShares = shareRepository.findAll();
        return  allShares.stream()
                 .filter(share -> share.getReceiverId().equals(user.getId()))
                .collect(Collectors.toList());
    }
}