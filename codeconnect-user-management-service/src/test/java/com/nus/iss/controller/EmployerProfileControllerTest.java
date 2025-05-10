package com.nus.iss.controller;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.AppUserMedia;
import com.nus.iss.entity.EmployerProfile;
import com.nus.iss.service.AppUserMediaService;
import com.nus.iss.service.EmployerProfileService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class EmployerProfileControllerTest {

    @InjectMocks
    private EmployerProfileController employerProfileController;

    @Mock
    private EmployerProfileService employerProfileService;

    @Mock
    private AppUserMediaService appUserMediaService;

    private AppUserDTO appUserDTO;
    private EmployerProfile employerProfile;
    private AppUserMedia appUserMedia;

    @BeforeEach
    void setUp() {
        appUserDTO = AppUserDTO.builder()
                .username("employer1")
                .email("employer1@example.com")
                .build();

        employerProfile = new EmployerProfile();
        employerProfile.setCompanyName("TechCorp");

        appUserMedia = new AppUserMedia();
        appUserMedia.setProfilePicture("profile-picture.jpg");
    }

    @Test
    void getAllEmployerProfiles() {
        List<AppUserDTO> employerProfiles = new ArrayList<>();
        employerProfiles.add(appUserDTO);

        when(employerProfileService.getAllEmployerProfiles()).thenReturn(employerProfiles);

        ResponseEntity<List<AppUserDTO>> response = employerProfileController.getAllEmployerProfiles();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(appUserDTO, response.getBody().get(0));
    }

    @Test
    void reviewEmployerProfile() {
        when(employerProfileService.reviewEmployerProfile(appUserDTO)).thenReturn(appUserDTO);

        ResponseEntity<AppUserDTO> response = employerProfileController.reviewEmployerProfile(appUserDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(appUserDTO, response.getBody());
    }

    @Test
    void deleteEmployerProfile() {
        doNothing().when(employerProfileService).deleteEmployerProfile("employer1");

        ResponseEntity<Void> response = employerProfileController.deleteEmployerProfile("employer1");

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void getEmployerProfile() {
        when(employerProfileService.getEmployerProfile("employer1")).thenReturn(appUserDTO);

        ResponseEntity<AppUserDTO> response = employerProfileController.getEmployerProfile("employer1", "EMPLOYER");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(appUserDTO, response.getBody());
    }

    @Test
    void updateEmployerProfile() {
        when(employerProfileService.updateEmployerProfile("employer1", employerProfile)).thenReturn(appUserDTO);

        ResponseEntity<AppUserDTO> response = employerProfileController.updateEmployerProfile("employer1", "EMPLOYER", employerProfile);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(appUserDTO, response.getBody());
    }

    @Test
    void getEmployerProfilePicture() {
        when(appUserMediaService.getProfilePicture("employer1")).thenReturn(appUserMedia);

        ResponseEntity<AppUserMedia> response = employerProfileController.getEmployerProfilePicture("employer1", "EMPLOYER");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(appUserMedia, response.getBody());
    }

    @Test
    void createEmployerProfilePicture() {
        MultipartFile file = mock(MultipartFile.class);
        when(appUserMediaService.createProfilePicture("employer1", file)).thenReturn("profile-picture.jpg");

        ResponseEntity<String> response = employerProfileController.createEmployerProfilePicture("employer1", "EMPLOYER", file);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("profile-picture.jpg", response.getBody());
    }

    @Test
    void deleteEmployerProfilePicture() {
        doNothing().when(appUserMediaService).deleteProfilePicture("employer1");

        ResponseEntity<Void> response = employerProfileController.deleteEmployerProfilePicture("employer1", "EMPLOYER");

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
    }
}