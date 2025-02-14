package com.nus.iss.service;

import java.io.IOException;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.web.multipart.MultipartFile;

import com.nus.iss.FileStorageService;
import com.nus.iss.Profile;
import com.nus.iss.ProfileRepository;
import com.nus.iss.ProfileService;

public class ProfileServiceTest {

    @Mock
    private ProfileRepository profileRepository;

    @Mock
    private FileStorageService fileStorageService;

    @InjectMocks
    private ProfileService profileService;

    @BeforeEach
    public void setUp() {
        MockitoAnnotations.openMocks(this);
    }

    @Test
    public void testCreateProfile() {
        Profile profile = new Profile();
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        Profile createdProfile = profileService.createProfile(profile);

        assertNotNull(createdProfile);
        verify(profileRepository, times(1)).save(profile);
    }

    @Test
    public void testGetProfileById() {
        Profile profile = new Profile();
        profile.setId(1L);
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));

        Optional<Profile> foundProfile = profileService.getProfileById(1L);

        assertNotNull(foundProfile);
        assertEquals(1L, foundProfile.get().getId());
    }

    @Test
    public void testUpdateProfile() {
        Profile profile = new Profile();
        profile.setId(1L);
        when(profileRepository.existsById(1L)).thenReturn(true);
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        Profile updatedProfile = profileService.updateProfile(1L, profile);

        assertNotNull(updatedProfile);
        assertEquals(1L, updatedProfile.getId());
        verify(profileRepository, times(1)).save(profile);
    }

    @Test
    public void testDeleteProfile() {
        profileService.deleteProfile(1L);
        verify(profileRepository, times(1)).deleteById(1L);
    }

    @Test
    public void testUploadResume() throws IOException {
        Profile profile = new Profile();
        profile.setId(1L);
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("resume.pdf");
        when(fileStorageService.storeResumeFile(file)).thenReturn("resume.pdf");

        Profile updatedProfile = profileService.uploadResume(1L, file);

        assertNotNull(updatedProfile);
        assertEquals("resume.pdf", updatedProfile.getResumeFileName());
        verify(profileRepository, times(1)).save(profile);
    }

    @Test
    public void testDeleteResume() throws IOException {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setResumeFileName("resume.pdf");
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(profileRepository.save(any(Profile.class))).thenReturn(profile);

        Profile updatedProfile = profileService.deleteResume(1L);

        assertNotNull(updatedProfile);
        assertNull(updatedProfile.getResumeFileName());
        verify(profileRepository, times(1)).save(profile);
    }

    @Test
    public void testGetResume() throws IOException {
        Profile profile = new Profile();
        profile.setId(1L);
        profile.setResumeFileName("resume.pdf");
        when(profileRepository.findById(1L)).thenReturn(Optional.of(profile));
        when(fileStorageService.getResumeFile("resume.pdf")).thenReturn(new byte[0]);

        byte[] resume = profileService.getResume(1L);

        assertNotNull(resume);
        verify(fileStorageService, times(1)).getResumeFile("resume.pdf");
    }
}