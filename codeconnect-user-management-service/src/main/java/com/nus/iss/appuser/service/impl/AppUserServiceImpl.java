package com.nus.iss.appuser.service.impl;

import com.nus.iss.appuser.config.security.JwtConfig;
import com.nus.iss.appuser.dto.AppUserDto;
import com.nus.iss.appuser.dto.JwtAccessTokenDTO;
import com.nus.iss.appuser.entity.AppUser;
import com.nus.iss.appuser.repository.AppUserRepository;
import com.nus.iss.appuser.service.AppUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
public class AppUserServiceImpl implements AppUserService {

    private final AppUserRepository appUserRepository;
    private final PasswordEncoder passwordEncoder;
    private final JwtConfig jwtConfig;
    private final JavaMailSender javaMailSender;

    @Autowired
    public AppUserServiceImpl(AppUserRepository appUserRepository, PasswordEncoder passwordEncoder, JwtConfig jwtConfig, JavaMailSender javaMailSender) {
        this.appUserRepository = appUserRepository;
        this.passwordEncoder = passwordEncoder;
        this.jwtConfig = jwtConfig;
        this.javaMailSender = javaMailSender;
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
                .status("INACTIVE")
                .email(appUser.getEmail())
                .build();
        AppUser savedAppUser = appUserRepository.save(user);
        javaMailSender.send(prepareEmail(savedAppUser));

        return savedAppUser;
    }

    @Override
    public JwtAccessTokenDTO login(String username, String password) {
        AppUser user = appUserRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Invalid username or password"));

        if (user.getStatus().equalsIgnoreCase("INACTIVE")) {
            throw new RuntimeException("User is inactive");
        }

        if (!passwordEncoder.matches(password, user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }

        return JwtAccessTokenDTO.builder()
                .accessToken(jwtConfig.generateToken(user.getUsername()))
                .build();
    }

    @Override
    public AppUser updatePassword(AppUserDto appUserDto) {
        AppUser user = appUserRepository.findByUsername(appUserDto.getUsername())
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!passwordEncoder.matches(appUserDto.getPassword(), user.getPassword())) {
            throw new RuntimeException("Invalid username or password");
        }
        user.setPassword(passwordEncoder.encode(appUserDto.getNewPassword()));
        return appUserRepository.save(user);
    }

    @Override
    public void activateUser(String token) {
        String decodedToken = new String(Base64.getDecoder().decode(token));
        AppUser user = appUserRepository.findByUsername(decodedToken)
                .orElseThrow(() -> new RuntimeException("Invalid token"));
        user.setStatus("ACTIVE");
        appUserRepository.save(user);
    }

    private SimpleMailMessage prepareEmail(AppUser savedAppUser) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(savedAppUser.getEmail());
        message.setSubject("Activate your CodeConnect account");
        message.setText("Please click the link to activate your account: http://localhost:8080/api/v1/activate?token="
                + Base64.getEncoder().encodeToString(savedAppUser.getUsername().getBytes()));
        return message;
    }

}
