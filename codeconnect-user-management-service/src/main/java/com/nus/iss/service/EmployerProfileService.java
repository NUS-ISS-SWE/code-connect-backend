package com.nus.iss.service;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.EmployerProfile;

import java.util.List;
import java.util.Optional;

public interface EmployerProfileService {
    List<AppUserDTO> getAllProfiles();

    AppUserDTO reviewEmployerProfile(AppUserDTO appUserDTO);

    EmployerProfile createProfile(EmployerProfile profile);

    Optional<EmployerProfile> getProfileById(Long id);

    EmployerProfile updateProfile(Long id, EmployerProfile updatedProfile);

    void deleteProfile(Long id);


}
