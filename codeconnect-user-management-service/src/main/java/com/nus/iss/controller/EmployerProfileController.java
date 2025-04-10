package com.nus.iss.controller;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.EmployerProfile;
import com.nus.iss.service.EmployerProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@Slf4j
@RequestMapping()
public class EmployerProfileController {

    private final EmployerProfileService employerProfileService;

    @Autowired
    public EmployerProfileController(EmployerProfileService employerProfileService) {
        this.employerProfileService = employerProfileService;
    }

    @GetMapping("/employer-profiles")
    public List<AppUserDTO> getAllProfiles() {
        log.info("Fetching all employer profiles");
        return employerProfileService.getAllProfiles();
    }

    @PostMapping("/review-employer-profiles")
    public ResponseEntity<AppUserDTO> reviewEmployerProfile(@RequestBody AppUserDTO appUserDTO) {
        log.info("Reviewing employer profile: {}", appUserDTO);
        return ResponseEntity.ok(employerProfileService.reviewEmployerProfile(appUserDTO));
    }

    @PostMapping("/employer-profiles")
    public EmployerProfile createProfile(@RequestBody EmployerProfile profile) {
        return employerProfileService.createProfile(profile);
    }

    @GetMapping("/{id}")
    public Optional<EmployerProfile> getProfileById(@PathVariable Long id) {
        return employerProfileService.getProfileById(id);
    }

    @PutMapping("/{id}")
    public EmployerProfile updateProfile(@PathVariable Long id, @RequestBody EmployerProfile profile) {
        return employerProfileService.updateProfile(id, profile);
    }

    @DeleteMapping("/employer-profiles")
    public void deleteProfile(@RequestParam String username) {
        log.info("Deleting employer profile: {}", username);
        employerProfileService.deleteProfile(username);
    }
}
