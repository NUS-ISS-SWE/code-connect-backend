package com.nus.iss.controller;

import com.nus.iss.model.EmployerProfile;
import com.nus.iss.service.EmployerProfileService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/employer-profiles")
public class EmployerProfileController {

    @Autowired
    private EmployerProfileService employerProfileService;

    @PostMapping
    public EmployerProfile createProfile(@RequestBody EmployerProfile profile) {
        return employerProfileService.createProfile(profile);
    }

    @GetMapping
    public List<EmployerProfile> getAllProfiles() {
        return employerProfileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public Optional<EmployerProfile> getProfileById(@PathVariable Long id) {
        return employerProfileService.getProfileById(id);
    }

    @PutMapping("/{id}")
    public EmployerProfile updateProfile(@PathVariable Long id, @RequestBody EmployerProfile profile) {
        return employerProfileService.updateProfile(id, profile);
    }

    @DeleteMapping("/{id}")
    public void deleteProfile(@PathVariable Long id) {
        employerProfileService.deleteProfile(id);
    }
}