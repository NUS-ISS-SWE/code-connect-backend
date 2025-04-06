package com.nus.iss.repository;

import com.nus.iss.model.EmployerProfile;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
public class EmployerProfileRepositoryTest {

    @Autowired
    private EmployerProfileRepository employerProfileRepository;

    @Test
    public void testSaveProfile() {
        EmployerProfile profile = new EmployerProfile();
        profile.setCompanyName("NUS");
        profile.setCompanySize("1000-5000");
        profile.setIndustry("Education");

        EmployerProfile savedProfile = employerProfileRepository.save(profile);

        assertEquals("NUS", savedProfile.getCompanyName());
        assertEquals("1000-5000", savedProfile.getCompanySize());
        assertEquals("Education", savedProfile.getIndustry());
    }

    @Test
    public void testFindById() {
        EmployerProfile profile = new EmployerProfile();
        profile.setCompanyName("NUS");
        EmployerProfile savedProfile = employerProfileRepository.save(profile);

        Optional<EmployerProfile> foundProfile = employerProfileRepository.findById(savedProfile.getId());

        assertTrue(foundProfile.isPresent());
        assertEquals("NUS", foundProfile.get().getCompanyName());
    }

    @Test
    public void testDeleteById() {
        EmployerProfile profile = new EmployerProfile();
        profile.setCompanyName("NUS");
        EmployerProfile savedProfile = employerProfileRepository.save(profile);

        employerProfileRepository.deleteById(savedProfile.getId());

        Optional<EmployerProfile> foundProfile = employerProfileRepository.findById(savedProfile.getId());

        assertTrue(foundProfile.isEmpty());
    }
}