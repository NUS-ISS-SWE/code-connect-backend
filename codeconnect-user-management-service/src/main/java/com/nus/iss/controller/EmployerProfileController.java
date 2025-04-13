package com.nus.iss.controller;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.AppUserMedia;
import com.nus.iss.entity.EmployerProfile;
import com.nus.iss.service.AppUserMediaService;
import com.nus.iss.service.EmployerProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class EmployerProfileController {

    private final EmployerProfileService employerProfileService;
    private final AppUserMediaService appUserMediaService;

    @Autowired
    public EmployerProfileController(EmployerProfileService employerProfileService,
                                     AppUserMediaService appUserMediaService) {
        this.employerProfileService = employerProfileService;
        this.appUserMediaService = appUserMediaService;
    }

    @GetMapping("/list-employer-profiles")
    public ResponseEntity<List<AppUserDTO>> getAllProfiles() {
        log.info("Fetching all employer profiles");
        return ResponseEntity.ok(employerProfileService.getAllProfiles());
    }

    @PostMapping("/review-employer-profiles")
    public ResponseEntity<AppUserDTO> reviewEmployerProfile(@RequestBody AppUserDTO appUserDTO) {
        log.info("Reviewing employer profile: {}", appUserDTO);
        return ResponseEntity.ok(employerProfileService.reviewEmployerProfile(appUserDTO));
    }

    @DeleteMapping("/employer-profiles")
    public ResponseEntity<Void> deleteEmployerProfile(@RequestParam String username) {
        log.info("Deleting employer profile: {}", username);
        employerProfileService.deleteEmployerProfile(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employer-profiles")
    public ResponseEntity<AppUserDTO> getEmployerProfile(@RequestHeader("X-Cdcnt-Username") String username,
                                                         @RequestHeader("X-Cdcnt-Role") String role) {
        log.info("Fetching employer profile for user: {}", username);
        AppUserDTO appUserDTO = employerProfileService.getEmployerProfile(username);
        return ResponseEntity.ok(appUserDTO);
    }

    @PutMapping("/employer-profiles")
    public ResponseEntity<AppUserDTO> updateEmployerProfile(@RequestHeader("X-Cdcnt-Username") String username,
                                                            @RequestHeader("X-Cdcnt-Role") String role,
                                                            @RequestBody EmployerProfile profile) {
        log.info("Updating employer profile for user: {}", username);
        AppUserDTO appUserDTO = employerProfileService.updateEmployerProfile(username, profile);
        return ResponseEntity.ok(appUserDTO);
    }

    @GetMapping("/employer-profiles-picture")
    public ResponseEntity<AppUserMedia> getEmployerProfilePicture(@RequestHeader("X-Cdcnt-Username") String username,
                                                                  @RequestHeader("X-Cdcnt-Role") String role) {
        log.info("Fetching profile picture for user: {}", username);
        AppUserMedia appUserMedia = appUserMediaService.getProfilePicture(username);
        if (null == appUserMedia) {
            log.warn("Profile picture is null for user: {}", username);
            return ResponseEntity.ok()
                    .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                    .body(null);
        } else {
            log.info("Profile picture found for user: {}", username);
            appUserMedia.setAppUser(null);
            return ResponseEntity.ok(appUserMedia);
        }
    }

    @PostMapping("/employer-profiles-picture")
    public ResponseEntity<String> createEmployerProfilePicture(@RequestHeader("X-Cdcnt-Username") String username,
                                                               @RequestHeader("X-Cdcnt-Role") String role,
                                                               @RequestParam("file") MultipartFile file) {
        String fileName = appUserMediaService.createProfilePicture(username, file);
        return ResponseEntity.ok(fileName);
    }

    @DeleteMapping("/employer-profiles-picture")
    public ResponseEntity<Void> deleteEmployerProfilePicture(@RequestHeader("X-Cdcnt-Username") String username,
                                                             @RequestHeader("X-Cdcnt-Role") String role) {
        log.info("Deleting profile picture for user: {}", username);
        appUserMediaService.deleteProfilePicture(username);
        return ResponseEntity.noContent().build();
    }

}
