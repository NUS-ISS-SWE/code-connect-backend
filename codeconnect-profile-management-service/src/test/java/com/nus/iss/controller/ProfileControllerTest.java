package com.nus.iss.controller;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.web.servlet.MockMvc;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.header;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import com.nus.iss.Profile;
import com.nus.iss.ProfileController;
import com.nus.iss.ProfileService;

@WebMvcTest(ProfileController.class)
public class ProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private ProfileService profileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProfile() throws Exception {
        Profile profile = new Profile();
        profile.setId(1L);
        when(profileService.createProfile(any(Profile.class))).thenReturn(profile);

        mockMvc.perform(post("/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testGetProfileById() throws Exception {
        Profile profile = new Profile();
        profile.setId(1L);
        when(profileService.getProfileById(1L)).thenReturn(Optional.of(profile));

        mockMvc.perform(get("/profiles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateProfile() throws Exception {
        Profile profile = new Profile();
        profile.setId(1L);
        when(profileService.updateProfile(any(Long.class), any(Profile.class))).thenReturn(profile);

        mockMvc.perform(put("/profiles/1")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testDeleteProfile() throws Exception {
        mockMvc.perform(delete("/profiles/1"))
                .andExpect(status().isOk());
    }

    @Test
    public void testUploadResume() throws Exception {
        Profile profile = new Profile();
        profile.setId(1L);
        MockMultipartFile file = new MockMultipartFile("file", "resume.pdf", MediaType.APPLICATION_PDF_VALUE, "Resume Content".getBytes());
        when(profileService.uploadResume(any(Long.class), any(MockMultipartFile.class))).thenReturn(profile);

        mockMvc.perform(multipart("/profiles/1/uploadResume").file(file))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testDeleteResume() throws Exception {
        Profile profile = new Profile();
        profile.setId(1L);
        when(profileService.deleteResume(any(Long.class))).thenReturn(profile);

        mockMvc.perform(delete("/profiles/1/deleteResume"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testGetResume() throws Exception {
        byte[] resumeContent = "Resume Content".getBytes();
        when(profileService.getResume(any(Long.class))).thenReturn(resumeContent);

        mockMvc.perform(get("/profiles/1/resume"))
                .andExpect(status().isOk())
                .andExpect(header().string("Content-Disposition", "attachment; filename=\"resume.pdf\""))
                .andExpect(content().bytes(resumeContent));
    }
}