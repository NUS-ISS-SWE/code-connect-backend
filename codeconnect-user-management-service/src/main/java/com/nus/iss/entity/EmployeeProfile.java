package com.nus.iss.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "employee_profile")
public class EmployeeProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
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

    @OneToOne
    @JoinColumn(name = "app_user_id", unique = true)
    @JsonBackReference
    private AppUser appUser;
}
