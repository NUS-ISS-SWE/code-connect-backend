package com.nus.iss.repository;

import java.io.IOException;
import java.util.Optional;

import com.nus.iss.model.Profile;
import com.nus.iss.service.FileStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.multipart.MultipartFile;


@Repository
public interface ProfileRepository extends JpaRepository<Profile, Long> {

    @Autowired
    FileStorageService fileStorageService = null;

    default Profile uploadResume(Long profileId, MultipartFile resumeFile) throws IOException {
        Profile profile = findById(profileId).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
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

    default Profile.ResumeData getResume(Long profileId) throws IOException {
        Profile profile = findById(profileId).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        byte[] resumeContent = fileStorageService.getResumeFile(profile.getResumeFileName());
        return new Profile.ResumeData(new String(resumeContent), profile.getResumeFileName());
    }

    Optional<Profile> findByUsername(String username);
}