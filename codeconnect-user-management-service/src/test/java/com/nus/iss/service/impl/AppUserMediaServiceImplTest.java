package com.nus.iss.service.impl;

import com.nus.iss.entity.AppUser;
import com.nus.iss.entity.AppUserMedia;
import com.nus.iss.repository.AppUserMediaRepository;
import com.nus.iss.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Base64;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class AppUserMediaServiceImplTest {

    @InjectMocks
    private AppUserMediaServiceImpl appUserMediaService;

    @Mock
    private AppUserRepository appUserRepository;

    @Mock
    private AppUserMediaRepository appUserMediaRepository;

    @Mock
    private MultipartFile file;

    private AppUser appUser;
    private AppUserMedia appUserMedia;

    @BeforeEach
    void setUp() {
        appUser = new AppUser();
        appUser.setUsername("testuser");

        appUserMedia = new AppUserMedia();
        appUserMedia.setProfilePicture("samplePicture");
        appUserMedia.setProfilePictureFileName("picture.jpg");
        appUserMedia.setResumeContent("sampleResume");
        appUserMedia.setResumeFileName("resume.pdf");
    }

    @Test
    void getProfilePicture() {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(appUserMediaRepository.findByAppUser(appUser)).thenReturn(Optional.of(appUserMedia));

        AppUserMedia result = appUserMediaService.getProfilePicture("testuser");

        assertNotNull(result);
        assertEquals("samplePicture", result.getProfilePicture());
        assertNull(result.getResumeContent());
        assertNull(result.getResumeFileName());
    }

    @Test
    void createProfilePicture() throws IOException {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(appUserMediaRepository.findByAppUser(appUser)).thenReturn(Optional.of(appUserMedia));
        when(file.getBytes()).thenReturn("fileContent".getBytes());
        when(file.getOriginalFilename()).thenReturn("picture.jpg");

        String result = appUserMediaService.createProfilePicture("testuser", file);

        assertEquals("Profile picture updated successfully", result);
        verify(appUserMediaRepository, times(1)).save(appUserMedia);
    }

    @Test
    void deleteProfilePicture() {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(appUserMediaRepository.findByAppUser(appUser)).thenReturn(Optional.of(appUserMedia));

        appUserMediaService.deleteProfilePicture("testuser");

        assertNull(appUserMedia.getProfilePicture());
        assertNull(appUserMedia.getProfilePictureFileName());
        verify(appUserMediaRepository, times(1)).save(appUserMedia);
    }

    @Test
    void getResume() {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(appUserMediaRepository.findByAppUser(appUser)).thenReturn(Optional.of(appUserMedia));

        AppUserMedia result = appUserMediaService.getResume("testuser");

        assertNotNull(result);
        assertEquals("sampleResume", result.getResumeContent());
        assertNull(result.getProfilePicture());
        assertNull(result.getProfilePictureFileName());
    }

    @Test
    void createResume() throws IOException {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(appUserMediaRepository.findByAppUser(appUser)).thenReturn(Optional.of(appUserMedia));
        when(file.getBytes()).thenReturn("fileContent".getBytes());
        when(file.getOriginalFilename()).thenReturn("resume.pdf");

        String result = appUserMediaService.createResume("testuser", file);

        assertEquals("Resume updated successfully", result);
        verify(appUserMediaRepository, times(1)).save(appUserMedia);
    }

    @Test
    void deleteResume() {
        when(appUserRepository.findByUsername("testuser")).thenReturn(Optional.of(appUser));
        when(appUserMediaRepository.findByAppUser(appUser)).thenReturn(Optional.of(appUserMedia));

        appUserMediaService.deleteResume("testuser");

        assertNull(appUserMedia.getResumeContent());
        assertNull(appUserMedia.getResumeFileName());
        verify(appUserMediaRepository, times(1)).save(appUserMedia);
    }
}