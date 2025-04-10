package com.nus.iss.service.impl;

import com.nus.iss.config.AppConstants;
import com.nus.iss.config.security.JwtConfig;
import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.dto.JwtAccessTokenDTO;
import com.nus.iss.entity.AppUser;
import com.nus.iss.entity.EmployeeProfile;
import com.nus.iss.entity.EmployerProfile;
import com.nus.iss.repository.AppUserRepository;
import com.nus.iss.repository.EmployeeProfileRepository;
import com.nus.iss.repository.EmployerProfileRepository;
import com.nus.iss.service.AppUserService;
import com.nus.iss.service.NotificationService;
import com.nus.iss.util.JsonMappingUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Slf4j
@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final EmployerProfileRepository employerProfileRepository;
    private final EmployeeProfileRepository employeeProfileRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final NotificationService notificationService;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository,
                              EmployerProfileRepository employerProfileRepository,
                              EmployeeProfileRepository employeeProfileRepository,
                              PasswordEncoder passwordEncoder,
                              JwtConfig jwtConfig,
                              NotificationService notificationService) {
        this.appUserRepository = appUserRepository;
        this.employerProfileRepository = employerProfileRepository;
        this.employeeProfileRepository = employeeProfileRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.notificationService = notificationService;
    }

    @Override
    public AppUserDTO registerUser(AppUserDTO appUserDTO) {
        validateIfUserExists(appUserDTO);
        if (AppConstants.EMPLOYER.equalsIgnoreCase(appUserDTO.getRole())) {
            AppUser appUser = JsonMappingUtil.appUserDTOtoAppUser(appUserDTO, AppConstants.EMPLOYER);
            appUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));

            AppUser savedAppUser = appUserRepository.save(appUser);
            EmployerProfile savedEmployer = employerProfileRepository.save(appUser.getEmployerProfile());

            return AppUserDTO.builder()
                    .id(savedAppUser.getId())
                    .username(savedAppUser.getUsername())
                    .email(savedAppUser.getEmail())
                    .role(savedAppUser.getRole())
                    .status(savedAppUser.getStatus())
                    .companyName(savedEmployer.getCompanyName())
                    .companySize(savedEmployer.getCompanySize())
                    .industry(savedEmployer.getIndustry())
                    .build();

        } else if (AppConstants.EMPLOYEE.equalsIgnoreCase(appUserDTO.getRole())) {
            AppUser appUser = JsonMappingUtil.appUserDTOtoAppUser(appUserDTO, AppConstants.EMPLOYEE);
            appUser.setPassword(passwordEncoder.encode(appUserDTO.getPassword()));

            AppUser savedAppUser = appUserRepository.save(appUser);
            EmployeeProfile savedEmployee = employeeProfileRepository.save(appUser.getEmployeeProfile());

            notificationService.sendActivationEmail(savedAppUser);

            return AppUserDTO.builder()
                    .id(savedAppUser.getId())
                    .username(savedAppUser.getUsername())
                    .role(savedAppUser.getRole())
                    .email(savedAppUser.getEmail())
                    .status(savedAppUser.getStatus())
                    .fullName(savedEmployee.getFullName())
                    .jobTitle(savedEmployee.getJobTitle())
                    .currentCompany(savedEmployee.getCurrentCompany())
                    .location(savedEmployee.getLocation())
                    .phone(savedEmployee.getPhone())
                    .aboutMe(savedEmployee.getAboutMe())
                    .programmingLanguages(savedEmployee.getProgrammingLanguages())
                    .education(savedEmployee.getEducation())
                    .experience(savedEmployee.getExperience())
                    .certifications(savedEmployee.getCertifications())
                    .skillSet(savedEmployee.getSkillSet())
                    .build();
        } else {
            throw new RuntimeException("Invalid role");
        }
    }

    @Override
    public JwtAccessTokenDTO login(String username, String password) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!AppConstants.ACTIVE.equalsIgnoreCase(user.getStatus())) {
            throw new RuntimeException("User is not activated yet.");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return JwtAccessTokenDTO.builder()
                .accessToken(jwtConfig.generateToken(user.getUsername(), user.getRole()))
                .role(user.getRole())
                .build();
    }

    @Override
    public AppUser updatePassword(AppUserDTO appUserDto) {
        AppUser user = appUserRepository.findByUsername(appUserDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(appUserDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        user.setPassword(passwordEncoder.encode(appUserDto.getNewPassword()));
        return appUserRepository.save(user);
    }

    @Override
    public void activateUser(String token) {
        String decodedToken = new String(Base64.getDecoder().decode(token));
        AppUser user = appUserRepository.findByUsername(decodedToken)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        if (AppConstants.INACTIVE.equalsIgnoreCase(user.getStatus()) || AppConstants.ACTIVE.equalsIgnoreCase(user.getStatus())) {
            user.setStatus(AppConstants.ACTIVE);
            appUserRepository.save(user);
        } else {
            throw new RuntimeException("User is pending review");
        }
    }


    private void validateIfUserExists(AppUserDTO appUserDTO) {
        if (appUserRepository.existsByUsername(appUserDTO.getUsername())) {
            throw new RuntimeException("Username already exists");
        }
        if (appUserRepository.existsByEmail(appUserDTO.getEmail())) {
            throw new RuntimeException("Email already exists");
        }
        log.info("Validation passed for user: {}", appUserDTO.getUsername());
    }
}
