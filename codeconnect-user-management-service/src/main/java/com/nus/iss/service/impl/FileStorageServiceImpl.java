package com.nus.iss.service.impl;

import com.nus.iss.service.FileStorageService;
import jakarta.annotation.PostConstruct;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

@Service
@Slf4j
public class FileStorageServiceImpl implements FileStorageService {

    private final String profileUploadDir = "uploads/profiles/";
    private final String resumeUploadDir = "uploads/resumes/";

    @PostConstruct
    public void init() {
        createDirectory(profileUploadDir);
        createDirectory(resumeUploadDir);
    }

    private void createDirectory(String dir) {
        File directory = new File(dir);
        if (!directory.exists()) {
            directory.mkdirs();
        }
    }

    public String storeProfileFile(MultipartFile file) throws IOException {
        return storeFile(file, profileUploadDir);
    }

    public String storeResumeFile(MultipartFile file) throws IOException {
        return storeFile(file, resumeUploadDir);
    }

    private String storeFile(MultipartFile file, String uploadDir) throws IOException {
        String fileName = file.getOriginalFilename();
        Path filePath = Paths.get(uploadDir + fileName);
        Files.write(filePath, file.getBytes());
        return fileName;
    }

    public void deleteProfileFile(String fileName) throws IOException {
        deleteFile(fileName, profileUploadDir);
    }

    public void deleteResumeFile(String fileName) throws IOException {
        deleteFile(fileName, resumeUploadDir);
    }

    private void deleteFile(String fileName, String uploadDir) throws IOException {
        Path filePath = Paths.get(uploadDir + fileName);
        Files.deleteIfExists(filePath);
    }

    public byte[] getResumeFile(String fileName) throws IOException {
        Path filePath = Paths.get(resumeUploadDir + fileName);
        return Files.readAllBytes(filePath);
    }
}
