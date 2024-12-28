package com.example.controller;


import com.example.service.FileService;
import com.example.model.User;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.Resource;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.FileNotFoundException;
import java.util.List;

@RestController
@RequestMapping("/fms/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    // Dosya yükleme
    @PostMapping("/upload/{userId}")
    public ResponseEntity<String> uploadFile(@PathVariable String userId, @RequestParam("file") MultipartFile file) {
        try {
            fileService.uploadFile(userId, file);
            return ResponseEntity.status(HttpStatus.OK).body("File uploaded successfully!");
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("File upload failed: " + e.getMessage());
        }
    }

    // Kullanıcıya ait dosyaları listeleme
    @GetMapping("/list/{userId}")
    public ResponseEntity<List<String>> listFiles(@PathVariable String userId) {
        try {
            List<String> files = fileService.listFiles(userId);
            return ResponseEntity.status(HttpStatus.OK).body(files);
        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }

    // Dosya indirme
    @GetMapping("/download/{userId}/{fileName}")
    public ResponseEntity<Resource> downloadFile(@PathVariable String userId, @PathVariable String fileName) {
        try {
            Resource file = fileService.downloadFile(userId, fileName);
            return ResponseEntity.ok().body(file);
        } catch (FileNotFoundException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(null);
        }
    }
}
