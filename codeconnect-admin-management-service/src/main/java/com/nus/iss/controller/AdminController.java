package com.nus.iss.controller;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.dto.JobPostingDTO;
import com.nus.iss.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/list-employer-profiles")
    public ResponseEntity<List<AppUserDTO>> getAllEmployerProfiles() {
        log.info("Fetching all employer profiles");
        List<AppUserDTO> employerProfiles = adminService.getAllEmployerProfiles();
        return ResponseEntity.ok(employerProfiles);
    }

    @GetMapping("/list-employee-profiles")
    public ResponseEntity<List<AppUserDTO>> getAllEmployeeProfiles() {
        log.info("Fetching all employee profiles");
        List<AppUserDTO> employerProfiles = adminService.getAllEmployeeProfiles();
        return ResponseEntity.ok(employerProfiles);
    }

    @PostMapping("/review-employer-profiles")
    public ResponseEntity<AppUserDTO> reviewEmployerProfile(@RequestBody AppUserDTO appUserDTO) {
        log.info("Reviewing employer profile: {}", appUserDTO);
        AppUserDTO reviewedProfile = adminService.reviewEmployerProfile(appUserDTO);
        return ResponseEntity.ok(reviewedProfile);
    }

    @DeleteMapping("/employer-profiles")
    public ResponseEntity<Void> deleteProfile(@RequestParam String username) {
        log.info("Deleting employer profile: {}", username);
        adminService.deleteProfile(username);
        return ResponseEntity.noContent().build();
    }

    @PostMapping("/review-job-posting")
    public ResponseEntity<JobPostingDTO> reviewJobPosting(@RequestBody JobPostingDTO jobPostingDTO) {
        log.info("Reviewing job posting: {}", jobPostingDTO);
        JobPostingDTO reviewJobPosting = adminService.reviewJobPosting(jobPostingDTO);
        return ResponseEntity.ok(reviewJobPosting);
    }
}
