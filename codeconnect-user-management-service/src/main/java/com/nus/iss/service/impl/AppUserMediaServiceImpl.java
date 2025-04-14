package com.nus.iss.service.impl;

import com.nus.iss.entity.AppUser;
import com.nus.iss.entity.AppUserMedia;
import com.nus.iss.repository.AppUserMediaRepository;
import com.nus.iss.repository.AppUserRepository;
import com.nus.iss.service.AppUserMediaService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

@Service
@Slf4j
public class AppUserMediaServiceImpl implements AppUserMediaService {
    private final AppUserRepository appUserRepository;
    private final AppUserMediaRepository appUserMediaRepository;

    @Autowired
    public AppUserMediaServiceImpl(AppUserMediaRepository appUserMediaRepository,
                                   AppUserRepository appUserRepository) {
        this.appUserMediaRepository = appUserMediaRepository;
        this.appUserRepository = appUserRepository;
    }

    @Override
    public AppUserMedia getProfilePicture(String username) {
        log.info("Fetching profile picture for user: {}", username);
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isPresent()) {
            Optional<AppUserMedia> appUserMedia = appUserMediaRepository.findByAppUser(appUser.get());
            if (appUserMedia.isPresent()) {
                if (null == appUserMedia.get().getProfilePicture()) {
                    log.warn("Profile picture is null for user: {}", username);
                    return null;
                } else {
                    log.info("Profile picture found for user: {}", username);
                    AppUserMedia media = appUserMedia.get();
                    media.setResumeContent(null);
                    media.setResumeFileName(null);
                    return media;
                }
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
        return null;
    }

    @Override
    public String createProfilePicture(String username, MultipartFile file) {
        log.info("Creating profile picture for user: {}", username);
        try {
            log.info("File bytes length: {}", file.getBytes().length);
            log.info("File name: {}", file.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isPresent()) {
            Optional<AppUserMedia> appUserMedia = appUserMediaRepository.findByAppUser(appUser.get());
            if (appUserMedia.isPresent()) {
                log.info("Updating profile picture for user: {}", username);
                try {
                    AppUserMedia appUserMedia1 = appUserMedia.get();
                    appUserMedia1.setProfilePicture(Base64.getEncoder().encodeToString(file.getBytes()));
                    appUserMedia1.setProfilePictureFileName(file.getOriginalFilename());
                    appUserMediaRepository.save(appUserMedia1);
                    return "Profile picture updated successfully";
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return file.getOriginalFilename();
    }

    @Override
    public void deleteProfilePicture(String username) {
        log.info("Deleting profile picture for user: {}", username);
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isPresent()) {
            Optional<AppUserMedia> appUserMedia = appUserMediaRepository.findByAppUser(appUser.get());
            if (appUserMedia.isPresent()) {
                AppUserMedia appUserMedia1 = appUserMedia.get();
                appUserMedia1.setProfilePicture(null);
                appUserMedia1.setProfilePictureFileName(null);
                appUserMediaRepository.save(appUserMedia1);
                log.info("Profile picture deleted successfully for user: {}", username);
            } else {
                log.warn("No profile picture found for user: {}", username);
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public AppUserMedia getResume(String username) {
        log.info("Fetching resume for user: {}", username);
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isPresent()) {
            Optional<AppUserMedia> appUserMedia = appUserMediaRepository.findByAppUser(appUser.get());
            if (appUserMedia.isPresent()) {
                if (null == appUserMedia.get().getResumeContent()) {
                    log.warn("Resume is null for user: {}", username);
                    return null;
                } else {
                    log.info("Resume found for user: {}", username);
                    AppUserMedia media = appUserMedia.get();
                    media.setProfilePictureFileName(null);
                    media.setProfilePicture(null);
                    return media;
                }
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
        return null;
    }

    @Override
    public String createResume(String username, MultipartFile file) {
        log.info("Creating resume for user: {}", username);
        try {
            log.info("File bytes length: {}", file.getBytes().length);
            log.info("File name: {}", file.getOriginalFilename());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isPresent()) {
            Optional<AppUserMedia> appUserMedia = appUserMediaRepository.findByAppUser(appUser.get());
            if (appUserMedia.isPresent()) {
                log.info("Updating resume for user: {}", username);
                try {
                    AppUserMedia appUserMedia1 = appUserMedia.get();
                    appUserMedia1.setResumeContent(Base64.getEncoder().encodeToString(file.getBytes()));
                    appUserMedia1.setResumeFileName(file.getOriginalFilename());
                    appUserMediaRepository.save(appUserMedia1);
                    return "Resume updated successfully";
                } catch (IOException e) {
                    throw new RuntimeException(e);
                }
            }
        }
        return file.getOriginalFilename();
    }

    @Override
    public void deleteResume(String username) {
        log.info("Deleting resume for user: {}", username);
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isPresent()) {
            Optional<AppUserMedia> appUserMedia = appUserMediaRepository.findByAppUser(appUser.get());
            if (appUserMedia.isPresent()) {
                AppUserMedia appUserMedia1 = appUserMedia.get();
                appUserMedia1.setResumeContent(null);
                appUserMedia1.setResumeFileName(null);
                appUserMediaRepository.save(appUserMedia1);
                log.info("Resume deleted successfully for user: {}", username);
            } else {
                log.warn("No resume found for user: {}", username);
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
