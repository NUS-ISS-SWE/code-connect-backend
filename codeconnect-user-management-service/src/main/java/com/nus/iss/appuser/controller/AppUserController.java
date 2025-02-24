package com.nus.iss.appuser.controller;

import com.nus.iss.appuser.dto.JwtAccessTokenDTO;
import com.nus.iss.appuser.entity.AppUser;
import com.nus.iss.appuser.service.AppUserService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/api/v1")
@RequiredArgsConstructor
public class AppUserController {
    private final AppUserService appUserService;

    @PostMapping("/register")
    public ResponseEntity<AppUser> register(@RequestBody AppUser appUser) {
        AppUser registeredUser = appUserService.registerUser(appUser);
        return ResponseEntity.ok(registeredUser);
    }

    @PostMapping("/login")
    public ResponseEntity<JwtAccessTokenDTO> login(@RequestBody AppUser appUser) {
        JwtAccessTokenDTO tokenDTO = appUserService.login(appUser.getUsername(), appUser.getPassword());
        return ResponseEntity.ok(tokenDTO);
    }

    @GetMapping("/test-token-admin")
    public ResponseEntity<String> testTokenAdmin() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok("Hello " + authentication.getPrincipal());
        } else {
            return ResponseEntity.ok("Hello Anonymous");
        }
    }

    @GetMapping("/test-token-user")
    public ResponseEntity<String> testTokenUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        if (authentication.isAuthenticated()) {
            return ResponseEntity.ok("Hello " + authentication.getPrincipal());
        } else {
            return ResponseEntity.ok("Hello Anonymous");
        }
    }
}
