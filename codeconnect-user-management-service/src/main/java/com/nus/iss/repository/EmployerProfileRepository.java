package com.nus.iss.repository;

import com.nus.iss.entity.AppUser;
import com.nus.iss.entity.EmployerProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployerProfileRepository extends JpaRepository<EmployerProfile, Long> {
    Optional<EmployerProfile> findByAppUser(AppUser appUser);
}