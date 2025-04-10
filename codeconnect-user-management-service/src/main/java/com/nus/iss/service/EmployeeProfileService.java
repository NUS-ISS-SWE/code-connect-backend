package com.nus.iss.service;

import com.nus.iss.entity.EmployeeProfile;

import java.util.List;
import java.util.Optional;

public interface EmployeeProfileService {
    EmployeeProfile createProfile(EmployeeProfile profile);

    List<EmployeeProfile> getAllProfiles();

    Optional<EmployeeProfile> getProfileById(Long id);

    EmployeeProfile updateProfile(Long id, EmployeeProfile updatedProfile);

    void deleteProfile(Long id);

}
