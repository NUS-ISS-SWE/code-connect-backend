package com.nus.iss.service;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.EmployerProfile;

import java.util.List;

public interface EmployerProfileService {
    List<AppUserDTO> getAllEmployerProfiles();

    AppUserDTO reviewEmployerProfile(AppUserDTO appUserDTO);

    void deleteEmployerProfile(String username);

    AppUserDTO getEmployerProfile(String username);

    AppUserDTO updateEmployerProfile(String username, EmployerProfile profile);
}
