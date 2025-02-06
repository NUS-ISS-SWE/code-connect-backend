package com.nus.iss.appuser.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.nus.iss.appuser.entity.AppUser;
import com.nus.iss.appuser.service.AppUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@ExtendWith(MockitoExtension.class)
class AppUserControllerTest {

    private MockMvc mockMvc;

    @Mock
    private AppUserService appUserService;

    @InjectMocks
    private AppUserController appUserController;

    private ObjectMapper objectMapper;
    private AppUser testUser;

    @BeforeEach
    void setUp() {
        mockMvc = MockMvcBuilders.standaloneSetup(appUserController).build();
        objectMapper = new ObjectMapper();

        testUser = AppUser.builder()
                .username("testuser")
                .password("password123")
                .role("USER")
                .build();
    }

    @Test
    void testRegisterUser_Success() throws Exception {
        when(appUserService.registerUser(any(AppUser.class))).thenReturn(testUser);

        mockMvc.perform(post("/api/v1/register")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(testUser))) // Convert Java object to JSON
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.username").value("testuser"))
                .andExpect(jsonPath("$.password").value("password123"))
                .andExpect(jsonPath("$.role").value("USER"));

        verify(appUserService, times(1)).registerUser(any(AppUser.class));
    }
}