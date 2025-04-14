package com.nus.iss.service.impl;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.AppUser;
import com.nus.iss.entity.EmployeeProfile;
import com.nus.iss.repository.AppUserRepository;
import com.nus.iss.repository.EmployeeProfileRepository;
import com.nus.iss.service.EmployeeProfileService;
import com.nus.iss.util.JsonMappingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployeeProfileServiceImpl implements EmployeeProfileService {
    private final AppUserRepository appUserRepository;
    private final EmployeeProfileRepository employeeProfileRepository;

    @Autowired
    public EmployeeProfileServiceImpl(AppUserRepository appUserRepository,
                                      EmployeeProfileRepository employeeProfileRepository) {
        this.appUserRepository = appUserRepository;
        this.employeeProfileRepository = employeeProfileRepository;
    }

    @Override
    public List<AppUserDTO> getAllEmployeeProfiles() {
        log.info("Fetching list of employee users");
        List<AppUserDTO> list = employeeProfileRepository.findAll()
                .stream()
                .map(user -> {
                    AppUserDTO appUserDTO = new AppUserDTO();
                    appUserDTO.setId(user.getAppUser().getId());
                    appUserDTO.setUsername(user.getAppUser().getUsername());
                    appUserDTO.setEmail(user.getAppUser().getEmail());
                    appUserDTO.setStatus(user.getAppUser().getStatus());
                    appUserDTO.setRole(user.getAppUser().getRole());

                    appUserDTO.setFullName(user.getFullName());
                    appUserDTO.setJobTitle(user.getJobTitle());
                    appUserDTO.setCurrentCompany(user.getCurrentCompany());
                    appUserDTO.setLocation(user.getLocation());
                    appUserDTO.setPhone(user.getPhone());
                    appUserDTO.setAboutMe(user.getAboutMe());
                    appUserDTO.setProgrammingLanguage(user.getProgrammingLanguage());
                    appUserDTO.setEducation(user.getEducation());
                    appUserDTO.setExperience(user.getExperience());
                    appUserDTO.setCertification(user.getCertification());
                    appUserDTO.setSkillSet(user.getSkillSet());
                    return appUserDTO;
                }).toList();
        log.info("List of employee users: {}", list);
        return list;
    }

    @Override
    public AppUserDTO getEmployeeProfile(String username) {
        log.info("Fetching employee profile for user: {}", username);
        AppUserDTO appUserDTO = new AppUserDTO();
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isPresent()) {
            AppUser user = appUser.get();
            Optional<EmployeeProfile> employeeProfile = employeeProfileRepository.findByAppUser(user);
            if (employeeProfile.isPresent()) {
                return JsonMappingUtil.employeeProfileToAppUserDTO(employeeProfile.get(), user);
            } else {
                throw new IllegalArgumentException("Employee profile not found");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public AppUserDTO updateEmployeeProfile(String username, EmployeeProfile profile) {
        log.info("Updating employee profile for user: {}", username);
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isPresent()) {
            AppUser user = appUser.get();
            Optional<EmployeeProfile> employeeProfile = employeeProfileRepository.findByAppUser(user);
            if (employeeProfile.isPresent()) {
                EmployeeProfile existingProfile = employeeProfile.get();
                existingProfile.setFullName(profile.getFullName());
                existingProfile.setJobTitle(profile.getJobTitle());
                existingProfile.setCurrentCompany(profile.getCurrentCompany());
                existingProfile.setLocation(profile.getLocation());
                existingProfile.setPhone(profile.getPhone());
                existingProfile.setAboutMe(profile.getAboutMe());
                existingProfile.setProgrammingLanguage(profile.getProgrammingLanguage());
                existingProfile.setEducation(profile.getEducation());
                existingProfile.setExperience(profile.getExperience());
                existingProfile.setCertification(profile.getCertification());
                existingProfile.setSkillSet(profile.getSkillSet());

                employeeProfileRepository.save(existingProfile);
                return JsonMappingUtil.employeeProfileToAppUserDTO(existingProfile, user);
            } else {
                throw new IllegalArgumentException("Employee profile not found");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }
}
