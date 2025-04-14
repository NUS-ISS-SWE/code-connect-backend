package com.nus.iss.controller;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.AppUserMedia;
import com.nus.iss.entity.EmployeeProfile;
import com.nus.iss.service.AppUserMediaService;
import com.nus.iss.service.EmployeeProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

import static org.springframework.http.MediaType.APPLICATION_OCTET_STREAM;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class EmployeeProfileController {
    private final EmployeeProfileService employeeProfileService;
    private final AppUserMediaService appUserMediaService;

    @Autowired
    public EmployeeProfileController(EmployeeProfileService employeeProfileService,
                                     AppUserMediaService appUserMediaService) {
        this.employeeProfileService = employeeProfileService;
        this.appUserMediaService = appUserMediaService;
    }


    @GetMapping("/list-employee-profiles")
    public ResponseEntity<List<AppUserDTO>> getAllEmployeeProfiles() {
        log.info("Fetching all employee profiles");
        return ResponseEntity.ok(employeeProfileService.getAllEmployeeProfiles());
    }

    @GetMapping("/employee-profiles")
    public ResponseEntity<AppUserDTO> getEmployeeProfile(@RequestHeader("X-Cdcnt-Username") String username,
                                                         @RequestHeader("X-Cdcnt-Role") String role) {
        log.info("Fetching employee profile for user: {}", username);
        AppUserDTO appUserDTO = employeeProfileService.getEmployeeProfile(username);
        return ResponseEntity.ok(appUserDTO);
    }

    @PutMapping("/employee-profiles")
    public ResponseEntity<AppUserDTO> updateEmployeeProfile(@RequestHeader("X-Cdcnt-Username") String username,
                                                            @RequestHeader("X-Cdcnt-Role") String role,
                                                            @RequestBody EmployeeProfile profile) {
        log.info("Updating employee profile for user: {}", username);
        AppUserDTO appUserDTO = employeeProfileService.updateEmployeeProfile(username, profile);
        return ResponseEntity.ok(appUserDTO);
    }

    @GetMapping("/employee-profiles-picture")
    public ResponseEntity<AppUserMedia> getEmployeeProfilePicture(@RequestHeader("X-Cdcnt-Username") String username,
                                                                  @RequestHeader("X-Cdcnt-Role") String role) {
        log.info("Fetching employee profile picture for user: {}", username);
        AppUserMedia appUserMedia = appUserMediaService.getProfilePicture(username);
        if (null == appUserMedia) {
            log.warn("Profile picture is null for user: {}", username);
            return ResponseEntity.ok()
                    .contentType(APPLICATION_OCTET_STREAM)
                    .body(null);
        } else {
            log.info("Profile picture found for user: {}", username);
            appUserMedia.setAppUser(null);
            return ResponseEntity.ok(appUserMedia);
        }
    }

    @PostMapping("/employee-profiles-picture")
    public ResponseEntity<String> uploadEmployeeProfilePicture(@RequestHeader("X-Cdcnt-Username") String username,
                                                               @RequestHeader("X-Cdcnt-Role") String role,
                                                               @RequestParam("file") MultipartFile file) {
        log.info("Uploading employee profile picture for user: {}", username);
        String fileName = appUserMediaService.createProfilePicture(username, file);
        return ResponseEntity.ok(fileName);
    }

    @DeleteMapping("/employee-profiles-picture")
    public ResponseEntity<Void> deleteEmployeeProfilePicture(@RequestHeader("X-Cdcnt-Username") String username,
                                                             @RequestHeader("X-Cdcnt-Role") String role) {
        log.info("Deleting profile picture for user: {}", username);
        appUserMediaService.deleteProfilePicture(username);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/employee-resume")
    public ResponseEntity<AppUserMedia> getEmployeeResume(@RequestHeader("X-Cdcnt-Username") String username,
                                                          @RequestHeader("X-Cdcnt-Role") String role) {
        log.info("Fetching employee resume for user: {}", username);
        AppUserMedia appUserMedia = appUserMediaService.getResume(username);
        if (null == appUserMedia) {
            log.warn("Resume is null for user: {}", username);
            return ResponseEntity.ok()
                    .contentType(APPLICATION_OCTET_STREAM)
                    .body(null);
        } else {
            log.info("Resume found for user: {}", username);
            appUserMedia.setAppUser(null);
            return ResponseEntity.ok(appUserMedia);
        }
    }

    @PostMapping("/employee-resume")
    public ResponseEntity<String> uploadEmployeeResume(@RequestHeader("X-Cdcnt-Username") String username,
                                                       @RequestHeader("X-Cdcnt-Role") String role,
                                                       @RequestParam("file") MultipartFile file) {
        log.info("Uploading employee resume for user: {}", username);
        String fileName = appUserMediaService.createResume(username, file);
        return ResponseEntity.ok(fileName);
    }

    @DeleteMapping("/employee-resume")
    public ResponseEntity<Void> deleteEmployeeResume(@RequestHeader("X-Cdcnt-Username") String username,
                                                     @RequestHeader("X-Cdcnt-Role") String role) {
        log.info("Deleting employee resume for user: {}", username);
        appUserMediaService.deleteResume(username);
        return ResponseEntity.noContent().build();
    }
}
