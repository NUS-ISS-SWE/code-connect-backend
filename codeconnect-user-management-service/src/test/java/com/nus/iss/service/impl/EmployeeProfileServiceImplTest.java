package com.nus.iss.service.impl;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.AppUser;
import com.nus.iss.entity.EmployeeProfile;
import com.nus.iss.repository.AppUserRepository;
import com.nus.iss.repository.EmployeeProfileRepository;
import com.nus.iss.util.JsonMappingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockedStatic;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployeeProfileServiceImplTest {

    @InjectMocks
    private EmployeeProfileServiceImpl employeeProfileService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private EmployeeProfileRepository employeeProfileRepository;

    private AppUser appUser;
    private EmployeeProfile employeeProfile;

    @BeforeEach
    void setUp() {
        appUser = new AppUser();
        appUser.setId(1L);
        appUser.setUsername("testuser");
        appUser.setEmail("testuser@example.com");
        appUser.setStatus("ACTIVE");
        appUser.setRole("EMPLOYEE");

        employeeProfile = new EmployeeProfile();
        employeeProfile.setAppUser(appUser);
        employeeProfile.setFullName("John Doe");
        employeeProfile.setJobTitle("Software Developer");
        employeeProfile.setCurrentCompany("TechCorp");
        employeeProfile.setLocation("New York");
        employeeProfile.setPhone("123-456-7890");
        employeeProfile.setAboutMe("Passionate about coding");
        employeeProfile.setProgrammingLanguage("Java, Python");
        employeeProfile.setEducation("BSc Computer Science");
        employeeProfile.setExperience("5 years");
        employeeProfile.setCertification("Oracle Certified Java Programmer");
        employeeProfile.setSkillSet("Java, Spring, SQL");
    }

    @Test
    void getAllEmployeeProfiles() {
        List<EmployeeProfile> profiles = new ArrayList<>();
        profiles.add(employeeProfile);

        when(employeeProfileRepository.findAll()).thenReturn(profiles);

        List<AppUserDTO> result = employeeProfileService.getAllEmployeeProfiles();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        verify(employeeProfileRepository, times(1)).findAll();
    }


    @Test
    void getEmployeeProfile_Success() {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUsername("testuser");
        appUserDTO.setFullName("John Doe");
        appUserDTO.setJobTitle("Software Developer");

        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(employeeProfileRepository.findByAppUser(appUser)).thenReturn(Optional.of(employeeProfile));

        // Mock the static method using Mockito's mockStatic
        try (MockedStatic<JsonMappingUtil> mockedStatic = mockStatic(JsonMappingUtil.class)) {
            mockedStatic.when(() -> JsonMappingUtil.employeeProfileToAppUserDTO(employeeProfile, appUser))
                    .thenReturn(appUserDTO);

            AppUserDTO result = employeeProfileService.getEmployeeProfile("testuser");

            assertNotNull(result);
            assertEquals("testuser", result.getUsername());
            assertEquals("John Doe", result.getFullName());
            verify(appUserRepository, times(1)).findByUsername("testuser");
            verify(employeeProfileRepository, times(1)).findByAppUser(appUser);
        }
    }

    @Test
    void getEmployeeProfile_UserNotFound() {
        when(appUserRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employeeProfileService.getEmployeeProfile("unknownuser"));

        assertEquals("User not found", exception.getMessage());
        verify(appUserRepository, times(1)).findByUsername("unknownuser");
    }

    @Test
    void getEmployeeProfile_ProfileNotFound() {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(employeeProfileRepository.findByAppUser(appUser)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employeeProfileService.getEmployeeProfile("testuser"));

        assertEquals("Employee profile not found", exception.getMessage());
        verify(appUserRepository, times(1)).findByUsername("testuser");
        verify(employeeProfileRepository, times(1)).findByAppUser(appUser);
    }

    @Test
    void updateEmployeeProfile_Success() {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUsername("testuser");
        appUserDTO.setFullName("Jane Doe");
        appUserDTO.setJobTitle("Senior Developer");

        EmployeeProfile updatedProfile = new EmployeeProfile();
        updatedProfile.setFullName("Jane Doe");
        updatedProfile.setJobTitle("Senior Developer");

        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(employeeProfileRepository.findByAppUser(appUser)).thenReturn(Optional.of(employeeProfile));
        when(employeeProfileRepository.save(employeeProfile)).thenReturn(employeeProfile);

        // Mock the static method using Mockito's mockStatic
        try (MockedStatic<JsonMappingUtil> mockedStatic = mockStatic(JsonMappingUtil.class)) {
            mockedStatic.when(() -> JsonMappingUtil.employeeProfileToAppUserDTO(employeeProfile, appUser))
                    .thenReturn(appUserDTO);

            AppUserDTO result = employeeProfileService.updateEmployeeProfile("testuser", updatedProfile);

            assertNotNull(result);
            assertEquals("Jane Doe", result.getFullName());
            assertEquals("Senior Developer", result.getJobTitle());
            verify(employeeProfileRepository, times(1)).save(employeeProfile);
        }
    }
    @Test
    void updateEmployeeProfile_UserNotFound() {
        when(appUserRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employeeProfileService.updateEmployeeProfile("unknownuser", employeeProfile));

        assertEquals("User not found", exception.getMessage());
        verify(appUserRepository, times(1)).findByUsername("unknownuser");
    }

    @Test
    void updateEmployeeProfile_ProfileNotFound() {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(employeeProfileRepository.findByAppUser(appUser)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employeeProfileService.updateEmployeeProfile("testuser", employeeProfile));

        assertEquals("Employee profile not found", exception.getMessage());
        verify(appUserRepository, times(1)).findByUsername("testuser");
        verify(employeeProfileRepository, times(1)).findByAppUser(appUser);
    }
}