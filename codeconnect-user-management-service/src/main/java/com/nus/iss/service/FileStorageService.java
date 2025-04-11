package com.nus.iss.service;

import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public interface FileStorageService {
    String storeProfileFile(MultipartFile file) throws IOException;

    String storeResumeFile(MultipartFile file) throws IOException;

    void deleteProfileFile(String fileName) throws IOException;

    void deleteResumeFile(String fileName) throws IOException;

    byte[] getResumeFile(String fileName) throws IOException;

}
