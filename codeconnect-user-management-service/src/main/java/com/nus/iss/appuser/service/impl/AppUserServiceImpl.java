package com.nus.iss.appuser.service.impl;

import com.nus.iss.appuser.config.security.JwtConfig;
import com.nus.iss.appuser.dto.JwtAccessTokenDTO;
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
    private final JwtConfig jwtConfig;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, JwtConfig jwtConfig) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
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

    @Override
    public JwtAccessTokenDTO login(String username, String password) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return JwtAccessTokenDTO.builder()
                .accessToken(jwtConfig.generateToken(user.getUsername()))
                .build();
    }

}
