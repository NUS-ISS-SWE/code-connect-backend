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
@Table(name = "employer_profile")
public class EmployerProfile {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String companyName;
    private String companyDescription;
    private Integer companySize;
    private String industry;
    private String companyLocation;

    @OneToOne
    @JoinColumn(name = "app_user_id", unique = true)
    @JsonBackReference
    private AppUser appUser;

}
