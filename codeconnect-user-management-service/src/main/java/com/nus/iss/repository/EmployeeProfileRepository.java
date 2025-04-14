package com.nus.iss.repository;

import com.nus.iss.entity.AppUser;
import com.nus.iss.entity.EmployeeProfile;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface EmployeeProfileRepository extends JpaRepository<EmployeeProfile, Long> {
    Optional<EmployeeProfile> findByAppUser(AppUser user);
}
