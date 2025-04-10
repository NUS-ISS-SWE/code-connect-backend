package com.nus.iss.controller;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.dto.JwtAccessTokenDTO;
import com.nus.iss.entity.AppUser;
import com.nus.iss.service.AppUserService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@Slf4j
@RestController
@RequestMapping("/api/v1")
public class AppUserController {
    private final AppUserService appUserService;

    @Autowired
    public AppUserController(AppUserService appUserService) {
        this.appUserService = appUserService;
    }

    @PostMapping("/register")
    public ResponseEntity<AppUserDTO> register(@RequestBody AppUserDTO appUserDTO) {
        log.info("Registering user: {}", appUserDTO);
        AppUserDTO registeredUser = appUserService.registerUser(appUserDTO);
        return ResponseEntity.ok(registeredUser);
    }

    @GetMapping("/activate")
    public ResponseEntity<String> activateUserAcc(@RequestParam String token) {
        appUserService.activateUser(token);
        return ResponseEntity.ok("User activated successfully");
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAccessTokenDTO> login(@RequestBody AppUserDTO appUserDTO) {
        JwtAccessTokenDTO tokenDTO = appUserService.login(appUserDTO.getUsername(), appUserDTO.getPassword());
        return ResponseEntity.ok(tokenDTO);
    }

    @PostMapping("/update-password")
    public ResponseEntity<AppUser> updatePassword(@RequestBody AppUserDTO appUserDto) {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if ("anonymousUser".equalsIgnoreCase((String) authentication.getPrincipal())) {
            throw new RuntimeException("Anonymous user cannot update password");
        } else if (!appUserDto.getUsername().equalsIgnoreCase((String) authentication.getPrincipal())) {
            throw new RuntimeException("User can update only their own password");
        } else {
            appUserDto.setUsername((String) authentication.getPrincipal());
            AppUser appUser = appUserService.updatePassword(appUserDto);
            return ResponseEntity.ok(appUser);
        }
    }
}
