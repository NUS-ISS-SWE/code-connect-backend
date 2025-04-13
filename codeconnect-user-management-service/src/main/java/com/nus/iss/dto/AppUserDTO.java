package com.nus.iss.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

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
    private String companyDescription;
    private Integer companySize;
    private String industry;
    private String companyLocation;

    // Employee Profile
    private String fullName;
    private String jobTitle;
    private String currentCompany;
    private String location;
    private String phone;
    private String aboutMe;
    private String programmingLanguage;
    private String education;
    private String experience;
    private String certification;
    private String skillSet;

}
