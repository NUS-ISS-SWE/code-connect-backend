package com.nus.iss.appuser.service.impl;

import com.nus.iss.appuser.entity.AppUser;
import com.nus.iss.appuser.repository.AppUserRepository;
import com.nus.iss.appuser.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
    }

    @Override
    public AppUser registerUser(AppUser appUser) {
        if (appUserRepository.findByUsername(appUser.getUsername()).isPresent()) {
            throw new RuntimeException("Username already exists");
        }

        AppUser user = AppUser.builder()
                .username(appUser.getUsername())
                .password(passwordEncoder.encode(appUser.getPassword()))
                .role("USER")
                .build();
        return appUserRepository.save(user);
    }
}
