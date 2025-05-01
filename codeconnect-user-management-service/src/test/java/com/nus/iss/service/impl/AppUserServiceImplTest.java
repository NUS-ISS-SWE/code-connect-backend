package com.nus.iss.service.impl;

import com.nus.iss.config.security.JwtConfig;
import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.dto.JwtAccessTokenDTO;
import com.nus.iss.entity.AppUser;
import com.nus.iss.entity.AppUserMedia;
import com.nus.iss.entity.EmployeeProfile;
import com.nus.iss.entity.EmployerProfile;
import com.nus.iss.repository.AppUserMediaRepository;
import com.nus.iss.repository.AppUserRepository;
import com.nus.iss.repository.EmployeeProfileRepository;
import com.nus.iss.repository.EmployerProfileRepository;
import com.nus.iss.service.NotificationService;
import com.nus.iss.util.JsonMappingUtil;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    @InjectMocks
    private AppUserServiceImpl appUserService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private EmployerProfileRepository employerProfileRepository;

    @Mock
    private EmployeeProfileRepository employeeProfileRepository;

    @Mock
    private AppUserMediaRepository appUserMediaRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private JwtConfig jwtConfig;

    @Mock
    private NotificationService notificationService;

    private AppUserDTO appUserDTO;
    private AppUser appUser;

    @BeforeEach
    void setUp() {
        appUserDTO = AppUserDTO.builder()
                .username("testuser")
                .password("password")
                .role("EMPLOYER")
                .email("testuser@example.com")
                .build();

        appUser = new AppUser();
        appUser.setUsername("testuser");
        appUser.setPassword("encodedPassword");
        appUser.setRole("EMPLOYER");
        appUser.setEmail("testuser@example.com");
        appUser.setStatus("ACTIVE");
    }

    @Test
    void registerUser_Employer() {
        when(appUserRepository.existsByUsername(appUserDTO.getUsername())).thenReturn(false);
        when(appUserRepository.existsByEmail(appUserDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(appUserDTO.getPassword())).thenReturn("encodedPassword");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
        when(employerProfileRepository.save(any(EmployerProfile.class))).thenReturn(new EmployerProfile());
        when(appUserMediaRepository.save(any(AppUserMedia.class))).thenReturn(new AppUserMedia());

        AppUserDTO result = appUserService.registerUser(appUserDTO);

        assertNotNull(result);
        assertEquals(appUserDTO.getUsername(), result.getUsername());
        verify(appUserRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    void registerUser_Employee() {
        appUserDTO.setRole("EMPLOYEE");

        when(appUserRepository.existsByUsername(appUserDTO.getUsername())).thenReturn(false);
        when(appUserRepository.existsByEmail(appUserDTO.getEmail())).thenReturn(false);
        when(passwordEncoder.encode(appUserDTO.getPassword())).thenReturn("encodedPassword");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);
        when(employeeProfileRepository.save(any(EmployeeProfile.class))).thenReturn(new EmployeeProfile());
        when(appUserMediaRepository.save(any(AppUserMedia.class))).thenReturn(new AppUserMedia());

        AppUserDTO result = appUserService.registerUser(appUserDTO);

        assertNotNull(result);
        assertEquals(appUserDTO.getUsername(), result.getUsername());
        verify(appUserRepository, times(1)).save(any(AppUser.class));
        verify(employeeProfileRepository, times(1)).save(any(EmployeeProfile.class));
    }

    @Test
    void login_Success() {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(jwtConfig.generateToken("testuser", "EMPLOYER")).thenReturn("jwtToken");

        JwtAccessTokenDTO result = appUserService.login("testuser", "password");

        assertNotNull(result);
        assertEquals("jwtToken", result.getAccessToken());
        assertEquals("EMPLOYER", result.getRole());
    }

    @Test
    void login_InvalidPassword() {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(passwordEncoder.matches("wrongPassword", "encodedPassword")).thenReturn(false);

        RuntimeException exception = assertThrows(RuntimeException.class, () -> appUserService.login("testuser", "wrongPassword"));
        assertEquals("Invalid username or password", exception.getMessage());
    }

    @Test
    void updatePassword_Success() {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(passwordEncoder.matches("password", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("newPassword")).thenReturn("newEncodedPassword");
        when(appUserRepository.save(any(AppUser.class))).thenReturn(appUser);

        appUserDTO.setNewPassword("newPassword");
        AppUser result = appUserService.updatePassword(appUserDTO);

        assertNotNull(result);
        verify(appUserRepository, times(1)).save(appUser);
    }

    @Test
    void activateUser_Success() {
        String token = Base64.getEncoder().encodeToString("testuser".getBytes());
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));

        appUserService.activateUser(token);

        assertEquals("ACTIVE", appUser.getStatus());
        verify(appUserRepository, times(1)).save(appUser);
    }

    @Test
    void activateUser_InvalidToken() {
        String token = Base64.getEncoder().encodeToString("invaliduser".getBytes());
        when(appUserRepository.findByUsername("invaliduser")).thenReturn(Optional.empty());

        RuntimeException exception = assertThrows(RuntimeException.class, () -> appUserService.activateUser(token));
        assertEquals("Invalid token", exception.getMessage());
    }
}