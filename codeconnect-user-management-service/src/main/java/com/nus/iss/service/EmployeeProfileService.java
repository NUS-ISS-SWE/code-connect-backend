package com.nus.iss.service;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.EmployeeProfile;

import java.util.List;

public interface EmployeeProfileService {

    List<AppUserDTO> getAllEmployeeProfiles();

    AppUserDTO getEmployeeProfile(String username);

    AppUserDTO updateEmployeeProfile(String username, EmployeeProfile profile);
}
