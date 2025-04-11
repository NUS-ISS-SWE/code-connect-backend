package com.nus.iss.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class AppUserDTO {
    private Long id;
    private String username;
    private String password;
    private String newPassword;
    private String role;
    private String email;
    private String status;

    // Employer Profile
    private String companyName;
    private String companySize;
    private String industry;

    // Employee Profile
    private String fullName;
    private String jobTitle;
    private String currentCompany;
    private String location;
    private String phone;
    private String aboutMe;
    private String programmingLanguages;
    private List<String> education;
    private List<String> experience;
    private List<String> certifications;
    private List<String> skillSet;

}
