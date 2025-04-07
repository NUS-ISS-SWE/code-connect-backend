package com.nus.iss.repository;

import java.io.IOException;
import java.util.Optional;

import com.nus.iss.model.EmployeeProfile;
import com.nus.iss.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;


@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {

    @Autowired
    FileStorageService fileStorageService = null;

    default EmployeeProfile uploadResume(Long profileId, MultipartFile resumeFile) throws IOException {
        EmployeeProfile profile = findById(profileId).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        if (resumeFile.isEmpty()) {
            if (profile.getResumeFileName() != null) {
                fileStorageService.deleteResumeFile(profile.getResumeFileName());
            }
            profile.setResume(null);
            profile.setResumeFileName(null);
        } else {
            String fileName = fileStorageService.storeResumeFile(resumeFile);
            profile.setResume("Resume uploaded");
            profile.setResumeFileName(fileName);
        }
        return save(profile);
    }

    default EmployeeProfile.ResumeData getResume(Long profileId) throws IOException {
        EmployeeProfile profile = findById(profileId).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        byte[] resumeContent = fileStorageService.getResumeFile(profile.getResumeFileName());
        return new EmployeeProfile.ResumeData(new String(resumeContent), profile.getResumeFileName());
    }

    Optional<EmployeeProfile> findByUsername(String username);
}