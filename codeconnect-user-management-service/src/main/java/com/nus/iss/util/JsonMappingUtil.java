package com.nus.iss.util;

import com.nus.iss.config.AppConstants;
import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.AppUser;
import com.nus.iss.entity.EmployeeProfile;
import com.nus.iss.entity.EmployerProfile;

public class JsonMappingUtil {
    private JsonMappingUtil() {
    }

    public static AppUser appUserDTOtoAppUser(AppUserDTO appUserDTO, String role) {
        AppUser appUser = new AppUser();
        appUser.setUsername(appUserDTO.getUsername());
        appUser.setEmail(appUserDTO.getEmail());
        appUser.setRole(role);
        if (AppConstants.EMPLOYER.equalsIgnoreCase(role)) {
            appUser.setStatus(AppConstants.PENDING_REVIEW);
            EmployerProfile employerProfile = new EmployerProfile();
            employerProfile.setCompanyName(appUserDTO.getCompanyName());
            employerProfile.setCompanySize(appUserDTO.getCompanySize());
            employerProfile.setIndustry(appUserDTO.getIndustry());
            employerProfile.setAppUser(appUser);
            appUser.setEmployerProfile(employerProfile);
            return appUser;
        } else if (AppConstants.EMPLOYEE.equalsIgnoreCase(role)) {
            appUser.setStatus(AppConstants.INACTIVE);
            EmployeeProfile employeeProfile = new EmployeeProfile();
            employeeProfile.setFullName(appUserDTO.getFullName());
            employeeProfile.setJobTitle(appUserDTO.getJobTitle());
            employeeProfile.setCurrentCompany(appUserDTO.getCurrentCompany());
            employeeProfile.setLocation(appUserDTO.getLocation());
            employeeProfile.setPhone(appUserDTO.getPhone());
            employeeProfile.setAboutMe(appUserDTO.getAboutMe());
            employeeProfile.setProgrammingLanguage(appUserDTO.getProgrammingLanguage());

            employeeProfile.setEducation(appUserDTO.getEducation());
            employeeProfile.setExperience(appUserDTO.getExperience());
            employeeProfile.setCertification(appUserDTO.getCertification());
            employeeProfile.setSkillSet(appUserDTO.getSkillSet());

            employeeProfile.setAppUser(appUser);
            appUser.setEmployeeProfile(employeeProfile);
            return appUser;
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }

    public static AppUserDTO employerProfileToAppUserDTO(EmployerProfile employerProfile, AppUser appUser) {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setId(appUser.getId());
        appUserDTO.setUsername(appUser.getUsername());
        appUserDTO.setEmail(appUser.getEmail());
        appUserDTO.setStatus(appUser.getStatus());
        appUserDTO.setRole(appUser.getRole());
        appUserDTO.setCompanyName(employerProfile.getCompanyName());
        appUserDTO.setCompanyDescription(employerProfile.getCompanyDescription());
        appUserDTO.setCompanySize(employerProfile.getCompanySize());
        appUserDTO.setIndustry(employerProfile.getIndustry());
        appUserDTO.setCompanyLocation(employerProfile.getCompanyLocation());
        return appUserDTO;
    }

    public static AppUserDTO employeeProfileToAppUserDTO(EmployeeProfile employeeProfile, AppUser appUser) {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setId(appUser.getId());
        appUserDTO.setUsername(appUser.getUsername());
        appUserDTO.setEmail(appUser.getEmail());
        appUserDTO.setStatus(appUser.getStatus());
        appUserDTO.setRole(appUser.getRole());
        appUserDTO.setFullName(employeeProfile.getFullName());
        appUserDTO.setJobTitle(employeeProfile.getJobTitle());
        appUserDTO.setCurrentCompany(employeeProfile.getCurrentCompany());
        appUserDTO.setLocation(employeeProfile.getLocation());
        appUserDTO.setPhone(employeeProfile.getPhone());
        appUserDTO.setAboutMe(employeeProfile.getAboutMe());
        appUserDTO.setProgrammingLanguage(employeeProfile.getProgrammingLanguage());
        appUserDTO.setEducation(employeeProfile.getEducation());
        appUserDTO.setExperience(employeeProfile.getExperience());
        appUserDTO.setCertification(employeeProfile.getCertification());
        appUserDTO.setSkillSet(employeeProfile.getSkillSet());

        return appUserDTO;
    }
}
