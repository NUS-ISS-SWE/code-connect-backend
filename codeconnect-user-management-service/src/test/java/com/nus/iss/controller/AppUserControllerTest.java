package com.nus.iss.controller;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.dto.JwtAccessTokenDTO;
import com.nus.iss.entity.AppUser;
import com.nus.iss.service.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserControllerTest {
    @InjectMocks
    private AppUserController appUserController;

    @Mock
    private AppUserService appUserService;

    private AppUserDTO employerUserDTO;
    private AppUserDTO employeeUserDTO;
    @BeforeEach
    void setUp() {
        employerUserDTO = new AppUserDTO();
        employerUserDTO = AppUserDTO.builder()
                .id(1L)
                .username("employer1")
                .password("password")
                .role("EMPLOYER")
                .email("john.doe@example.com")
                .status("ACTIVE")
                .companyName("TechCorp")
                .companyDescription("Tech company specializing in AI solutions")
                .companySize(500)
                .industry("Technology")
                .companyLocation("Singapore")
                .build();
        employeeUserDTO = new AppUserDTO();
        employeeUserDTO = AppUserDTO.builder()
                .id(1L)
                .username("employee1")
                .password("password")
                .role("EMPLOYEE")
                .email("john.doe@example.com")
                .status("ACTIVE")
                .fullName("John Doe")
                .jobTitle("Software Developer")
                .currentCompany("Tech Inc.")
                .location("New York")
                .phone("123-456-7890")
                .aboutMe("Passionate about coding")
                .programmingLanguage("Java, Python")
                .education("BSc Computer Science")
                .experience("5 years in software development")
                .certification("Oracle Certified Java Programmer")
                .skillSet("Java, Spring, Python, SQL")
                .build();
    }


    @Test
    void register() {
        when(appUserService.registerUser(employerUserDTO)).thenReturn(employerUserDTO);
        ResponseEntity<AppUserDTO> register = appUserController.register(employerUserDTO);
        assertNotNull(register);
        assertEquals(200, register.getStatusCodeValue());
        assertEquals(employerUserDTO, register.getBody());
    }

    @Test
    void activateUserAcc() {
        String token = "sample-activation-token";

        doNothing().when(appUserService).activateUser(token);

        ResponseEntity<String> response = appUserController.activateUserAcc(token);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("User activated successfully", response.getBody());
    }

    @Test
    void login() {
        JwtAccessTokenDTO jwtAccessTokenDTO = new JwtAccessTokenDTO();
        jwtAccessTokenDTO.setAccessToken("sample-jwt-token");

        when(appUserService.login(employerUserDTO.getUsername(), employerUserDTO.getPassword()))
                .thenReturn(jwtAccessTokenDTO);

        ResponseEntity<JwtAccessTokenDTO> response = appUserController.login(employerUserDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals("sample-jwt-token", response.getBody().getAccessToken());
    }


    @Test
    void updatePassword() {
        AppUser updatedUser = new AppUser();
        updatedUser.setUsername(employerUserDTO.getUsername());
        updatedUser.setPassword("new-password");

        Authentication authentication = mock(Authentication.class);
        when(authentication.getPrincipal()).thenReturn(employerUserDTO.getUsername());
        SecurityContextHolder.getContext().setAuthentication(authentication);

        when(appUserService.updatePassword(employerUserDTO)).thenReturn(updatedUser);

        ResponseEntity<AppUser> response = appUserController.updatePassword(employerUserDTO);

        assertNotNull(response);
        assertEquals(200, response.getStatusCodeValue());
        assertEquals(updatedUser, response.getBody());
    }
}