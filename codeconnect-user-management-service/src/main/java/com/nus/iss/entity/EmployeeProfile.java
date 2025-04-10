package com.nus.iss.entity;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;

import java.util.List;

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
    private String programmingLanguages;
    @ElementCollection
    private List<String> education;
    @ElementCollection
    private List<String> experience;
    @ElementCollection
    private List<String> certifications;
    @ElementCollection
    private List<String> skillSet;
    @Lob
    private byte[] profilePicture;
    @JsonIgnore
    private String resume;
    @JsonIgnore
    private String resumeFileName;

    @OneToOne
    @JoinColumn(name = "app_user_id", unique = true)
    @JsonBackReference
    private AppUser appUser;


    @AllArgsConstructor
    @NoArgsConstructor
    @Getter
    @Setter
    public static class ResumeData {
        private String resumeContent;
        private String resumeFileName;
    }

}
