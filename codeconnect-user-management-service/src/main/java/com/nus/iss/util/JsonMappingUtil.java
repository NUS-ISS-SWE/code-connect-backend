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
            employeeProfile.setProgrammingLanguages(appUserDTO.getProgrammingLanguages());

            employeeProfile.setEducation(appUserDTO.getEducation());
            employeeProfile.setExperience(appUserDTO.getExperience());
            employeeProfile.setCertifications(appUserDTO.getCertifications());
            employeeProfile.setSkillSet(appUserDTO.getSkillSet());

            employeeProfile.setAppUser(appUser);
            appUser.setEmployeeProfile(employeeProfile);
            return appUser;
        } else {
            throw new IllegalArgumentException("Invalid role: " + role);
        }
    }
}
