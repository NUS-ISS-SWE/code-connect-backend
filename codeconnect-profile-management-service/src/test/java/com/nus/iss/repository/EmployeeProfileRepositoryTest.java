package com.nus.iss.repository;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import com.nus.iss.model.EmployeeProfile;
import com.nus.iss.service.FileStorageService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.mock.mockito.MockBean;

@DataJpaTest
public class EmployeeProfileRepositoryTest {

    @Autowired
    private EmployeeProfileRepository profileRepository;

    @MockBean
    private FileStorageService fileStorageService;

    @Test
    public void testSaveProfile() {
        EmployeeProfile profile = new EmployeeProfile();
        profile.setFullName("John Doe");
        EmployeeProfile savedProfile = profileRepository.save(profile);

        assertEquals("John Doe", savedProfile.getFullName());
    }

    @Test
    public void testFindById() {
        EmployeeProfile profile = new EmployeeProfile();
        profile.setFullName("John Doe");
        EmployeeProfile savedProfile = profileRepository.save(profile);

        Optional<EmployeeProfile> foundProfile = profileRepository.findById(savedProfile.getId());

        assertTrue(foundProfile.isPresent());
        assertEquals("John Doe", foundProfile.get().getFullName());
    }

    @Test
    public void testDeleteById() {
        EmployeeProfile profile = new EmployeeProfile();
        profile.setFullName("John Doe");
        EmployeeProfile savedProfile = profileRepository.save(profile);

        profileRepository.deleteById(savedProfile.getId());

        Optional<EmployeeProfile> foundProfile = profileRepository.findById(savedProfile.getId());

        assertTrue(foundProfile.isEmpty());
    }
}