package com.nus.iss.util;

import com.nus.iss.config.AppConstants;
import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.AppUser;
import com.nus.iss.entity.EmployeeProfile;
import com.nus.iss.entity.EmployerProfile;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JsonMappingUtilTest {

    @Test
    void appUserDTOtoAppUser_Employer_Success() {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUsername("employerUser");
        appUserDTO.setEmail("employer@example.com");
        appUserDTO.setCompanyName("TechCorp");
        appUserDTO.setCompanySize(500);
        appUserDTO.setIndustry("Technology");

        AppUser appUser = JsonMappingUtil.appUserDTOtoAppUser(appUserDTO, AppConstants.EMPLOYER);

        assertNotNull(appUser);
        assertEquals("employerUser", appUser.getUsername());
        assertEquals("employer@example.com", appUser.getEmail());
        assertEquals(AppConstants.EMPLOYER, appUser.getRole());
        assertEquals(AppConstants.PENDING_REVIEW, appUser.getStatus());
        assertNotNull(appUser.getEmployerProfile());
        assertEquals("TechCorp", appUser.getEmployerProfile().getCompanyName());
        assertEquals(500, appUser.getEmployerProfile().getCompanySize());
        assertEquals("Technology", appUser.getEmployerProfile().getIndustry());
    }

    @Test
    void appUserDTOtoAppUser_Employee_Success() {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUsername("employeeUser");
        appUserDTO.setEmail("employee@example.com");
        appUserDTO.setFullName("John Doe");
        appUserDTO.setJobTitle("Software Engineer");
        appUserDTO.setCurrentCompany("TechCorp");
        appUserDTO.setLocation("Singapore");
        appUserDTO.setPhone("123456789");
        appUserDTO.setAboutMe("Passionate developer");
        appUserDTO.setProgrammingLanguage("Java");
        appUserDTO.setEducation("Bachelor's in Computer Science");
        appUserDTO.setExperience("5 years");
        appUserDTO.setCertification("AWS Certified");
        appUserDTO.setSkillSet("Spring Boot, Hibernate");

        AppUser appUser = JsonMappingUtil.appUserDTOtoAppUser(appUserDTO, AppConstants.EMPLOYEE);

        assertNotNull(appUser);
        assertEquals("employeeUser", appUser.getUsername());
        assertEquals("employee@example.com", appUser.getEmail());
        assertEquals(AppConstants.EMPLOYEE, appUser.getRole());
        assertEquals(AppConstants.INACTIVE, appUser.getStatus());
        assertNotNull(appUser.getEmployeeProfile());
        assertEquals("John Doe", appUser.getEmployeeProfile().getFullName());
        assertEquals("Software Engineer", appUser.getEmployeeProfile().getJobTitle());
        assertEquals("TechCorp", appUser.getEmployeeProfile().getCurrentCompany());
        assertEquals("Singapore", appUser.getEmployeeProfile().getLocation());
        assertEquals("123456789", appUser.getEmployeeProfile().getPhone());
        assertEquals("Passionate developer", appUser.getEmployeeProfile().getAboutMe());
        assertEquals("Java", appUser.getEmployeeProfile().getProgrammingLanguage());
        assertEquals("Bachelor's in Computer Science", appUser.getEmployeeProfile().getEducation());
        assertEquals("5 years", appUser.getEmployeeProfile().getExperience());
        assertEquals("AWS Certified", appUser.getEmployeeProfile().getCertification());
        assertEquals("Spring Boot, Hibernate", appUser.getEmployeeProfile().getSkillSet());
    }

    @Test
    void employerProfileToAppUserDTO_Success() {
        AppUser appUser = new AppUser();
        appUser.setId(1L);
        appUser.setUsername("employerUser");
        appUser.setEmail("employer@example.com");
        appUser.setStatus(AppConstants.PENDING_REVIEW);
        appUser.setRole(AppConstants.EMPLOYER);

        EmployerProfile employerProfile = new EmployerProfile();
        employerProfile.setCompanyName("TechCorp");
        employerProfile.setCompanyDescription("A leading tech company");
        employerProfile.setCompanySize(500);
        employerProfile.setIndustry("Technology");
        employerProfile.setCompanyLocation("Singapore");

        AppUserDTO appUserDTO = JsonMappingUtil.employerProfileToAppUserDTO(employerProfile, appUser);

        assertNotNull(appUserDTO);
        assertEquals(1L, appUserDTO.getId());
        assertEquals("employerUser", appUserDTO.getUsername());
        assertEquals("employer@example.com", appUserDTO.getEmail());
        assertEquals(AppConstants.PENDING_REVIEW, appUserDTO.getStatus());
        assertEquals(AppConstants.EMPLOYER, appUserDTO.getRole());
        assertEquals("TechCorp", appUserDTO.getCompanyName());
        assertEquals("A leading tech company", appUserDTO.getCompanyDescription());
        assertEquals(500, appUserDTO.getCompanySize());
        assertEquals("Technology", appUserDTO.getIndustry());
        assertEquals("Singapore", appUserDTO.getCompanyLocation());
    }

    @Test
    void employeeProfileToAppUserDTO_Success() {
        AppUser appUser = new AppUser();
        appUser.setId(1L);
        appUser.setUsername("employeeUser");
        appUser.setEmail("employee@example.com");
        appUser.setStatus(AppConstants.INACTIVE);
        appUser.setRole(AppConstants.EMPLOYEE);

        EmployeeProfile employeeProfile = new EmployeeProfile();
        employeeProfile.setFullName("John Doe");
        employeeProfile.setJobTitle("Software Engineer");
        employeeProfile.setCurrentCompany("TechCorp");
        employeeProfile.setLocation("Singapore");
        employeeProfile.setPhone("123456789");
        employeeProfile.setAboutMe("Passionate developer");
        employeeProfile.setProgrammingLanguage("Java");
        employeeProfile.setEducation("Bachelor's in Computer Science");
        employeeProfile.setExperience("5 years");
        employeeProfile.setCertification("AWS Certified");
        employeeProfile.setSkillSet("Spring Boot, Hibernate");

        AppUserDTO appUserDTO = JsonMappingUtil.employeeProfileToAppUserDTO(employeeProfile, appUser);

        assertNotNull(appUserDTO);
        assertEquals(1L, appUserDTO.getId());
        assertEquals("employeeUser", appUserDTO.getUsername());
        assertEquals("employee@example.com", appUserDTO.getEmail());
        assertEquals(AppConstants.INACTIVE, appUserDTO.getStatus());
        assertEquals(AppConstants.EMPLOYEE, appUserDTO.getRole());
        assertEquals("John Doe", appUserDTO.getFullName());
        assertEquals("Software Engineer", appUserDTO.getJobTitle());
        assertEquals("TechCorp", appUserDTO.getCurrentCompany());
        assertEquals("Singapore", appUserDTO.getLocation());
        assertEquals("123456789", appUserDTO.getPhone());
        assertEquals("Passionate developer", appUserDTO.getAboutMe());
        assertEquals("Java", appUserDTO.getProgrammingLanguage());
        assertEquals("Bachelor's in Computer Science", appUserDTO.getEducation());
        assertEquals("5 years", appUserDTO.getExperience());
        assertEquals("AWS Certified", appUserDTO.getCertification());
        assertEquals("Spring Boot, Hibernate", appUserDTO.getSkillSet());
    }
}