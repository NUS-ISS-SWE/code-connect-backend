package com.nus.iss.repository;

import com.nus.iss.entity.AppUser;
import com.nus.iss.entity.AppUserMedia;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface AppUserMediaRepository extends JpaRepository<AppUserMedia, Long> {
    Optional<AppUserMedia> findByAppUser(AppUser appUser);
}
