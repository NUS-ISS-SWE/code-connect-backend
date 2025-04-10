package com.nus.iss.controller;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@Slf4j
@RequestMapping("/api/v1")
public class AdminController {

    private final AdminService adminService;

    @Autowired
    public AdminController(AdminService adminService) {
        this.adminService = adminService;
    }

    @GetMapping("/employer-profiles")
    public ResponseEntity<List<AppUserDTO>> getAllEmployerProfiles() {
        log.info("Fetching all employer profiles");
        List<AppUserDTO> employerProfiles = adminService.getAllEmployerProfiles();
        return ResponseEntity.ok(employerProfiles);
    }
}
