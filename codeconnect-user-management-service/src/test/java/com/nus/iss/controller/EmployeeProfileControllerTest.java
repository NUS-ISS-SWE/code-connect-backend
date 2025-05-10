package com.nus.iss.controller;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.entity.AppUserMedia;
import com.nus.iss.entity.EmployeeProfile;
import com.nus.iss.service.AppUserMediaService;
import com.nus.iss.service.EmployeeProfileService;
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
class EmployeeProfileControllerTest {

    @InjectMocks
    private EmployeeProfileController employeeProfileController;

    @Mock
    private EmployeeProfileService employeeProfileService;

    @Mock
    private AppUserMediaService appUserMediaService;

    private AppUserDTO appUserDTO;
    private EmployeeProfile employeeProfile;
    private AppUserMedia appUserMedia;

    @BeforeEach
    void setUp() {
        appUserDTO = AppUserDTO.builder()
                .username("employee1")
                .email("employee1@example.com")
                .build();

        employeeProfile = new EmployeeProfile();
        employeeProfile.setJobTitle("Software Developer");

        appUserMedia = new AppUserMedia();
        appUserMedia.setProfilePicture("profile-picture.jpg");
    }

    @Test
    void getAllEmployeeProfiles() {
        List<AppUserDTO> employeeProfiles = new ArrayList<>();
        employeeProfiles.add(appUserDTO);

        when(employeeProfileService.getAllEmployeeProfiles()).thenReturn(employeeProfiles);

        ResponseEntity<List<AppUserDTO>> response = employeeProfileController.getAllEmployeeProfiles();

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(1, response.getBody().size());
        assertEquals(appUserDTO, response.getBody().get(0));
    }

    @Test
    void getEmployeeProfile() {
        when(employeeProfileService.getEmployeeProfile("employee1")).thenReturn(appUserDTO);

        ResponseEntity<AppUserDTO> response = employeeProfileController.getEmployeeProfile("employee1", "EMPLOYEE");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(appUserDTO, response.getBody());
    }

    @Test
    void updateEmployeeProfile() {
        when(employeeProfileService.updateEmployeeProfile("employee1", employeeProfile)).thenReturn(appUserDTO);

        ResponseEntity<AppUserDTO> response = employeeProfileController.updateEmployeeProfile("employee1", "EMPLOYEE", employeeProfile);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(appUserDTO, response.getBody());
    }

    @Test
    void getEmployeeProfilePicture() {
        when(appUserMediaService.getProfilePicture("employee1")).thenReturn(appUserMedia);

        ResponseEntity<AppUserMedia> response = employeeProfileController.getEmployeeProfilePicture("employee1", "EMPLOYEE");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(appUserMedia, response.getBody());
    }

    @Test
    void uploadEmployeeProfilePicture() {
        MultipartFile file = mock(MultipartFile.class);
        when(appUserMediaService.createProfilePicture("employee1", file)).thenReturn("profile-picture.jpg");

        ResponseEntity<String> response = employeeProfileController.uploadEmployeeProfilePicture("employee1", "EMPLOYEE", file);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("profile-picture.jpg", response.getBody());
    }

    @Test
    void deleteEmployeeProfilePicture() {
        doNothing().when(appUserMediaService).deleteProfilePicture("employee1");

        ResponseEntity<Void> response = employeeProfileController.deleteEmployeeProfilePicture("employee1", "EMPLOYEE");

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
    }

    @Test
    void getEmployeeResume() {
        when(appUserMediaService.getResume("employee1")).thenReturn(appUserMedia);

        ResponseEntity<AppUserMedia> response = employeeProfileController.getEmployeeResume("employee1", "EMPLOYEE");

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(appUserMedia, response.getBody());
    }

    @Test
    void uploadEmployeeResume() {
        MultipartFile file = mock(MultipartFile.class);
        when(appUserMediaService.createResume("employee1", file)).thenReturn("resume.pdf");

        ResponseEntity<String> response = employeeProfileController.uploadEmployeeResume("employee1", "EMPLOYEE", file);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("resume.pdf", response.getBody());
    }

    @Test
    void deleteEmployeeResume() {
        doNothing().when(appUserMediaService).deleteResume("employee1");

        ResponseEntity<Void> response = employeeProfileController.deleteEmployeeResume("employee1", "EMPLOYEE");

        assertNotNull(response);
        assertEquals(204, response.getStatusCodeValue());
    }
}