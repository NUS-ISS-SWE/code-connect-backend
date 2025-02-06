package com.nus.iss.appuser.service.impl;

import com.nus.iss.appuser.entity.AppUser;
import com.nus.iss.appuser.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserServiceImplTest {

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AppUserServiceImpl appUserService;

    private AppUser testUser;

    @BeforeEach
    void setUp() {
        testUser = AppUser.builder()
                .username("testuser")
                .password("password123")
                .build();
    }

    @Test
    void testRegisterUser_Success() {
        when(appUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.empty());

        when(passwordEncoder.encode(testUser.getPassword())).thenReturn("encodedPassword");

        AppUser savedUser = AppUser.builder()
                .username(testUser.getUsername())
                .password("encodedPassword")
                .role("USER")
                .build();

        when(appUserRepository.save(any(AppUser.class))).thenReturn(savedUser);

        AppUser registeredUser = appUserService.registerUser(testUser);

        assertNotNull(registeredUser);
        assertEquals("testuser", registeredUser.getUsername());
        assertEquals("encodedPassword", registeredUser.getPassword());
        assertEquals("USER", registeredUser.getRole());

        verify(appUserRepository, times(1)).findByUsername("testuser");
        verify(passwordEncoder, times(1)).encode("password123");
        verify(appUserRepository, times(1)).save(any(AppUser.class));
    }

    @Test
    void testRegisterUser_Failure_UsernameAlreadyExists() {
        when(appUserRepository.findByUsername(testUser.getUsername())).thenReturn(Optional.of(testUser));

        Exception exception = assertThrows(RuntimeException.class, () -> {
            appUserService.registerUser(testUser);
        });

        assertEquals("Username already exists", exception.getMessage());

        verify(appUserRepository, times(1)).findByUsername("testuser");
        verify(appUserRepository, never()).save(any(AppUser.class));
        verify(passwordEncoder, never()).encode(anyString());
    }
}
