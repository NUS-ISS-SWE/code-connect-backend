package com.nus.iss.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import com.nus.iss.Profile;
import com.nus.iss.ProfileRepository;

@DataJpaTest
public class ProfileRepositoryTest {

    @Autowired
    private ProfileRepository profileRepository;

    @Test
    public void testSaveProfile() {
        Profile profile = new Profile();
        profile.setFullName("John Doe");
        Profile savedProfile = profileRepository.save(profile);

        assertEquals("John Doe", savedProfile.getFullName());
    }

    @Test
    public void testFindById() {
        Profile profile = new Profile();
        profile.setFullName("John Doe");
        Profile savedProfile = profileRepository.save(profile);

        Optional<Profile> foundProfile = profileRepository.findById(savedProfile.getId());

        assertTrue(foundProfile.isPresent());
        assertEquals("John Doe", foundProfile.get().getFullName());
    }

    @Test
    public void testDeleteById() {
        Profile profile = new Profile();
        profile.setFullName("John Doe");
        Profile savedProfile = profileRepository.save(profile);

        profileRepository.deleteById(savedProfile.getId());

        Optional<Profile> foundProfile = profileRepository.findById(savedProfile.getId());

        assertTrue(foundProfile.isEmpty());
    }
}