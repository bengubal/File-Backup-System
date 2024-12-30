package com.example.repository;


import com.example.model.Share;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ShareRepository extends MongoRepository<Share, String> {
   List<Share> findByReceiverId(String receiverId);
}