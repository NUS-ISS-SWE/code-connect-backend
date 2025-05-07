package com.nus.iss.service.impl;

import com.nus.iss.config.AppConstants;
import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.AppUser;
import com.nus.iss.entity.EmployerProfile;
import com.nus.iss.repository.AppUserRepository;
import com.nus.iss.repository.EmployerProfileRepository;
import com.nus.iss.service.NotificationService;
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
class EmployerProfileServiceImplTest {

    @InjectMocks
    private EmployerProfileServiceImpl employerProfileService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private EmployerProfileRepository employerProfileRepository;

    @Mock
    private NotificationService notificationService;

    private AppUser appUser;
    private EmployerProfile employerProfile;

    @BeforeEach
    void setUp() {
        appUser = new AppUser();
        appUser.setId(1L);
        appUser.setUsername("testuser");
        appUser.setEmail("testuser@example.com");
        appUser.setStatus("ACTIVE");
        appUser.setRole("EMPLOYER");

        employerProfile = new EmployerProfile();
        employerProfile.setAppUser(appUser);
        employerProfile.setCompanyName("TechCorp");
        employerProfile.setCompanyDescription("A leading tech company");
        employerProfile.setCompanySize(500);
        employerProfile.setIndustry("Technology");
        employerProfile.setCompanyLocation("New York");
    }

    @Test
    void getAllEmployerProfiles() {
        List<EmployerProfile> profiles = new ArrayList<>();
        profiles.add(employerProfile);

        when(employerProfileRepository.findAll()).thenReturn(profiles);

        List<AppUserDTO> result = employerProfileService.getAllEmployerProfiles();

        assertNotNull(result);
        assertEquals(1, result.size());
        assertEquals("testuser", result.get(0).getUsername());
        verify(employerProfileRepository, times(1)).findAll();
    }

    @Test
    void reviewEmployerProfile_Success() {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUsername("testuser");

        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(appUserRepository.save(appUser)).thenReturn(appUser);

        AppUserDTO result = employerProfileService.reviewEmployerProfile(appUserDTO);

        assertNotNull(result);
        assertEquals(AppConstants.INACTIVE, result.getStatus());
        verify(notificationService, times(1)).sendActivationEmail(appUser);
    }

    @Test
    void reviewEmployerProfile_UserNotFound() {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUsername("unknownuser");

        when(appUserRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employerProfileService.reviewEmployerProfile(appUserDTO));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void deleteEmployerProfile_Success() {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));

        employerProfileService.deleteEmployerProfile("testuser");

        verify(appUserRepository, times(1)).deleteById(appUser.getId());
        verify(notificationService, times(1)).sendDeletionEmail(appUser);
    }

    @Test
    void deleteEmployerProfile_UserNotFound() {
        when(appUserRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employerProfileService.deleteEmployerProfile("unknownuser"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getEmployerProfile_Success() {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(employerProfileRepository.findByAppUser(appUser)).thenReturn(Optional.of(employerProfile));

        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUsername("testuser");
        appUserDTO.setCompanyName("TechCorp");
        appUserDTO.setCompanyDescription("A leading tech company");

        // Mock the static method using Mockito's mockStatic
        try (MockedStatic<JsonMappingUtil> mockedStatic = mockStatic(JsonMappingUtil.class)) {
            mockedStatic.when(() -> JsonMappingUtil.employerProfileToAppUserDTO(employerProfile, appUser))
                    .thenReturn(appUserDTO);

            AppUserDTO result = employerProfileService.getEmployerProfile("testuser");

            assertNotNull(result);
            assertEquals("testuser", result.getUsername());
            assertEquals("TechCorp", result.getCompanyName());
            assertEquals("A leading tech company", result.getCompanyDescription());
        }
    }
    @Test
    void getEmployerProfile_UserNotFound() {
        when(appUserRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employerProfileService.getEmployerProfile("unknownuser"));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void getEmployerProfile_ProfileNotFound() {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(employerProfileRepository.findByAppUser(appUser)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employerProfileService.getEmployerProfile("testuser"));

        assertEquals("Employer profile not found", exception.getMessage());
    }

    @Test
    void updateEmployerProfile_Success() {
        EmployerProfile updatedProfile = new EmployerProfile();
        updatedProfile.setCompanyName("NewTechCorp");
        updatedProfile.setCompanyDescription("Updated description");

        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(employerProfileRepository.findByAppUser(appUser)).thenReturn(Optional.of(employerProfile));
        when(employerProfileRepository.save(employerProfile)).thenReturn(employerProfile);

        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUsername("testuser");
        appUserDTO.setCompanyName("NewTechCorp");
        appUserDTO.setCompanyDescription("Updated description");

        // Mock the static method using Mockito's mockStatic
        try (MockedStatic<JsonMappingUtil> mockedStatic = mockStatic(JsonMappingUtil.class)) {
            mockedStatic.when(() -> JsonMappingUtil.employerProfileToAppUserDTO(employerProfile, appUser))
                    .thenReturn(appUserDTO);

            AppUserDTO result = employerProfileService.updateEmployerProfile("testuser", updatedProfile);

            assertNotNull(result);
            assertEquals("NewTechCorp", result.getCompanyName());
            assertEquals("Updated description", result.getCompanyDescription());
            verify(employerProfileRepository, times(1)).save(employerProfile);
        }
    }
    @Test
    void updateEmployerProfile_UserNotFound() {
        when(appUserRepository.findByUsername("unknownuser")).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employerProfileService.updateEmployerProfile("unknownuser", employerProfile));

        assertEquals("User not found", exception.getMessage());
    }

    @Test
    void updateEmployerProfile_ProfileNotFound() {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(employerProfileRepository.findByAppUser(appUser)).thenReturn(Optional.empty());

        IllegalArgumentException exception = assertThrows(IllegalArgumentException.class,
                () -> employerProfileService.updateEmployerProfile("testuser", employerProfile));

        assertEquals("Employer profile not found", exception.getMessage());
    }
}