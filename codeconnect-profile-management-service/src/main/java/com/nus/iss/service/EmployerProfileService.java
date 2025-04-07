package com.nus.iss.service;

import com.nus.iss.model.EmployerProfile;
import com.nus.iss.repository.EmployerProfileRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class EmployerProfileService {

    @Autowired
    private EmployerProfileRepository employerProfileRepository;

    public EmployerProfile createProfile(EmployerProfile profile) {
        return employerProfileRepository.save(profile);
    }

    public List<EmployerProfile> getAllProfiles() {
        return employerProfileRepository.findAll();
    }

    public Optional<EmployerProfile> getProfileById(Long id) {
        return employerProfileRepository.findById(id);
    }

    public EmployerProfile updateProfile(Long id, EmployerProfile updatedProfile) {
        EmployerProfile profile = employerProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("EmployerProfile not found"));
        profile.setCompanyName(updatedProfile.getCompanyName());
        profile.setCompanySize(updatedProfile.getCompanySize());
        profile.setIndustry(updatedProfile.getIndustry());
        return employerProfileRepository.save(profile);
    }

    public void deleteProfile(Long id) {
        employerProfileRepository.deleteById(id);
    }
}