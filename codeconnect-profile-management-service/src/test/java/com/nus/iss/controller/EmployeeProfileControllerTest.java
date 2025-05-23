package com.nus.iss.controller;

import java.util.Optional;

import com.nus.iss.model.EmployeeProfile;
import com.nus.iss.service.FileStorageService;
import com.nus.iss.service.EmployeeProfileService;
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
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import org.springframework.web.multipart.MultipartFile;

@WebMvcTest(EmployeeProfileController.class)
public class EmployeeProfileControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private EmployeeProfileService profileService;

    @MockBean
    private FileStorageService fileStorageService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProfile() throws Exception {
        EmployeeProfile profile = new EmployeeProfile();
        profile.setId(1L);
        when(profileService.createProfile(any(EmployeeProfile.class))).thenReturn(profile);

        mockMvc.perform(post("/profiles")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"id\":1}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testGetProfileById() throws Exception {
        EmployeeProfile profile = new EmployeeProfile();
        profile.setId(1L);
        when(profileService.getProfileById(1L)).thenReturn(Optional.of(profile));

        mockMvc.perform(get("/profiles/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testUpdateProfile() throws Exception {
        EmployeeProfile profile = new EmployeeProfile();
        profile.setId(1L);
        when(profileService.updateProfile(any(Long.class), any(EmployeeProfile.class))).thenReturn(profile);

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
        EmployeeProfile profile = new EmployeeProfile();
        profile.setFullName("John Doe");
        when(profileService.uploadResume(any(Long.class), any(MultipartFile.class))).thenReturn(profile);

        MockMultipartFile resumeFile = new MockMultipartFile("file", "resume.pdf", "application/pdf", "resume content".getBytes());
        when(fileStorageService.storeResumeFile(resumeFile)).thenReturn("resume.pdf");

        mockMvc.perform(multipart("/profiles/1/uploadResume").file(resumeFile))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.fullName").value("John Doe"));
    }

    @Test
    public void testDeleteResume() throws Exception {
        EmployeeProfile profile = new EmployeeProfile();
        profile.setId(1L);
        when(profileService.deleteResume(any(Long.class))).thenReturn(profile);

        mockMvc.perform(delete("/profiles/1/deleteResume"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1));
    }

    @Test
    public void testGetResume() throws Exception {
        EmployeeProfile profile = new EmployeeProfile();
        profile.setFullName("John Doe");
        when(profileService.getResume(any(Long.class))).thenReturn(new byte[0]);

        mockMvc.perform(get("/profiles/1/resume"))
                .andExpect(status().isOk())
                .andExpect(content().bytes(new byte[0]));
    }
}