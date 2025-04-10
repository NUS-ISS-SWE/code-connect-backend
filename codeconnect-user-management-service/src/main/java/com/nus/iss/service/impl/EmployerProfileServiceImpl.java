package com.nus.iss.service.impl;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.EmployerProfile;
import com.nus.iss.repository.EmployerProfileRepository;
import com.nus.iss.service.EmployerProfileService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployerProfileServiceImpl implements EmployerProfileService {

    private final EmployerProfileRepository employerProfileRepository;

    @Autowired
    public EmployerProfileServiceImpl(EmployerProfileRepository employerProfileRepository) {
        this.employerProfileRepository = employerProfileRepository;
    }

    @Override
    public EmployerProfile createProfile(EmployerProfile profile) {
        return employerProfileRepository.save(profile);
    }

    @Override
    public List<AppUserDTO> getAllProfiles() {
        log.info("Fetching list of employer users");
        List<AppUserDTO> list = employerProfileRepository.findAll()
                .stream()
                .map(user -> {
                    AppUserDTO appUserDTO = new AppUserDTO();
                    appUserDTO.setId(user.getAppUser().getId());
                    appUserDTO.setUsername(user.getAppUser().getUsername());
                    appUserDTO.setEmail(user.getAppUser().getEmail());
                    appUserDTO.setStatus(user.getAppUser().getStatus());
                    appUserDTO.setRole(user.getAppUser().getRole());
                    appUserDTO.setCompanyName(user.getCompanyName());
                    appUserDTO.setCompanySize(user.getCompanySize());
                    appUserDTO.setIndustry(user.getIndustry());
                    return appUserDTO;
                }).toList();
        log.info("List of employer users: {}", list);
        return list;
    }

    @Override
    public Optional<EmployerProfile> getProfileById(Long id) {
        return employerProfileRepository.findById(id);
    }

    @Override
    public EmployerProfile updateProfile(Long id, EmployerProfile updatedProfile) {
        EmployerProfile profile = employerProfileRepository.findById(id)
                .orElseThrow(() -> new IllegalArgumentException("EmployerProfile not found"));
        profile.setCompanyName(updatedProfile.getCompanyName());
        profile.setCompanySize(updatedProfile.getCompanySize());
        profile.setIndustry(updatedProfile.getIndustry());
        return employerProfileRepository.save(profile);
    }

    @Override
    public void deleteProfile(Long id) {
        employerProfileRepository.deleteById(id);
    }
}
