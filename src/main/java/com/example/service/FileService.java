package com.example.service;

import java.io.FileNotFoundException;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.List;
import org.springframework.core.io.UrlResource;
import org.springframework.core.io.Resource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import com.example.model.User;
import com.example.repository.UserRepository;


@Service
public class FileService {

    private final UserRepository userRepository;
    private final Path rootLocation = Paths.get("uploads");

    public FileService(UserRepository userRepository) {
        this.userRepository = userRepository;
        try {
            Files.createDirectories(rootLocation);
        } catch (IOException e) {
            throw new RuntimeException("Could not initialize storage location!", e);
        }
    }

    public void uploadFile(String userId, MultipartFile file) throws IOException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        Path rootLocation = Paths.get("uploads");
        String fileName = file.getOriginalFilename();
        Path destinationFile = rootLocation.resolve(fileName);
        Files.copy(file.getInputStream(), destinationFile, StandardCopyOption.REPLACE_EXISTING);

        // Kullanıcının dosya listesini güncelle
        if (user.getUploadedFiles() == null) {
            user.setUploadedFiles(new ArrayList<>());
        }
        user.getUploadedFiles().add(fileName);
        userRepository.save(user);
    }

    public List<String> listFiles(String userId) {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        return user.getUploadedFiles() != null ? user.getUploadedFiles() : new ArrayList<>();
    }

    public Resource downloadFile(String userId, String fileName) throws FileNotFoundException {
        User user = userRepository.findById(userId)
                .orElseThrow(() -> new RuntimeException("User not found"));

        if (user.getUploadedFiles() != null && user.getUploadedFiles().contains(fileName)) {
            try {
                Path filePath = rootLocation.resolve(fileName).normalize();
                
                // Check if the file exists and is readable
                if (Files.exists(filePath) && Files.isReadable(filePath)) {
                    Resource resource = new UrlResource(filePath.toUri());
                     if (resource.exists() && resource.isReadable()) {
                         return resource;
                     }else {
                          throw new FileNotFoundException("File is not readable or does not exist");
                     }
                } else {
                    throw new FileNotFoundException("File does not exist or is not readable");
                }
            } catch (MalformedURLException e) {
                throw new FileNotFoundException("Failed to create resource from file path: " + fileName);
            }
        } else {
           throw new FileNotFoundException("File not found for user: " + userId + " with filename: " + fileName );
        }
    }
}
