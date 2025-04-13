package com.nus.iss.service.impl;

import com.nus.iss.config.AppConstants;
import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.AppUser;
import com.nus.iss.entity.EmployerProfile;
import com.nus.iss.repository.AppUserRepository;
import com.nus.iss.repository.EmployerProfileRepository;
import com.nus.iss.service.EmployerProfileService;
import com.nus.iss.service.NotificationService;
import com.nus.iss.util.JsonMappingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
public class EmployerProfileServiceImpl implements EmployerProfileService {

    private final AppUserRepository appUserRepository;
    private final EmployerProfileRepository employerProfileRepository;
    private final NotificationService notificationService;

    @Autowired
    public EmployerProfileServiceImpl(EmployerProfileRepository employerProfileRepository, AppUserRepository appUserRepository,
                                      NotificationService notificationService) {
        this.employerProfileRepository = employerProfileRepository;
        this.appUserRepository = appUserRepository;
        this.notificationService = notificationService;
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
                    appUserDTO.setCompanyDescription(user.getCompanyDescription());
                    appUserDTO.setCompanySize(user.getCompanySize());
                    appUserDTO.setIndustry(user.getIndustry());
                    appUserDTO.setCompanyLocation(user.getCompanyLocation());
                    return appUserDTO;
                }).toList();
        log.info("List of employer users: {}", list);
        return list;
    }

    @Override
    public AppUserDTO reviewEmployerProfile(AppUserDTO appUserDTO) {
        log.info("Reviewing employer profile: {}", appUserDTO.getUsername());
        Optional<AppUser> byUsername = appUserRepository.findByUsername(appUserDTO.getUsername());
        if (byUsername.isPresent()) {
            AppUser appUser = byUsername.get();
            appUser.setStatus(AppConstants.INACTIVE);
            AppUser savedAppUser = appUserRepository.save(appUser);
            appUserDTO.setStatus(AppConstants.INACTIVE);
            notificationService.sendActivationEmail(savedAppUser);
            log.info("Reviewed employer profile: {}, employer can now proceed to activate their account.", appUserDTO.getUsername());
            return appUserDTO;
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public void deleteEmployerProfile(String username) {
        log.info("Deleting employer profile: {}", username);
        Optional<AppUser> byUsername = appUserRepository.findByUsername(username);
        if (byUsername.isPresent()) {
            AppUser appUser = byUsername.get();
            appUserRepository.deleteById(appUser.getId());
            log.info("Deleted employer profile: {}", username);
            notificationService.sendDeletionEmail(appUser);
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public AppUserDTO getEmployerProfile(String username) {
        log.info("Fetching employer profile for user: {}", username);
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isPresent()) {
            AppUser appUser1 = appUser.get();
            Optional<EmployerProfile> employerProfile = employerProfileRepository.findByAppUser(appUser1);
            if (employerProfile.isPresent()) {
                return JsonMappingUtil.employerProfileToAppUserDTO(employerProfile.get(), appUser1);
            } else {
                throw new IllegalArgumentException("Employer profile not found");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }

    @Override
    public AppUserDTO updateEmployerProfile(String username, EmployerProfile profile) {
        log.info("Updating employer profile for user: {}", username);
        Optional<AppUser> appUser = appUserRepository.findByUsername(username);
        if (appUser.isPresent()) {
            AppUser user = appUser.get();
            Optional<EmployerProfile> employerProfile = employerProfileRepository.findByAppUser(user);
            if (employerProfile.isPresent()) {
                EmployerProfile employerProfile1 = employerProfile.get();
                employerProfile1.setCompanyName(profile.getCompanyName());
                employerProfile1.setCompanyDescription(profile.getCompanyDescription());
                employerProfile1.setCompanySize(profile.getCompanySize());
                employerProfile1.setIndustry(profile.getIndustry());
                EmployerProfile savedEmployerProfile = employerProfileRepository.save(employerProfile1);
                return JsonMappingUtil.employerProfileToAppUserDTO(savedEmployerProfile, user);
            } else {
                throw new IllegalArgumentException("Employer profile not found");
            }
        } else {
            throw new IllegalArgumentException("User not found");
        }
    }


}
