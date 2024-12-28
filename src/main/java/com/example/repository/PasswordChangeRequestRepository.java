package com.example.repository;

import com.example.model.PasswordChangeRequest;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;
import java.util.List;
import java.util.Optional;
@Repository
public interface PasswordChangeRequestRepository extends MongoRepository<PasswordChangeRequest,String>{
        List<PasswordChangeRequest> findByStatus(String status);
       
}