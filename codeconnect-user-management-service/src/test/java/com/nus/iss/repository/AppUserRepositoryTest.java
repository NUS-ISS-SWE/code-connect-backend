package com.nus.iss.repository;

import com.nus.iss.entity.AppUser;
import com.nus.iss.repository.AppUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;

import static org.junit.jupiter.api.Assertions.assertEquals;

@DataJpaTest
class AppUserRepositoryTest {

    @Autowired
    private AppUserRepository appUserRepository;


    @BeforeEach
    void setUp() {
        AppUser testUser = AppUser.builder()
                .username("testuser")
                .password("password123")
                .build();
        appUserRepository.save(testUser);
    }

    @Test
    public void testFindByUsername() {
        AppUser appUser = appUserRepository.findByUsername("testuser").get();
        assertEquals("testuser", appUser.getUsername());
    }
}