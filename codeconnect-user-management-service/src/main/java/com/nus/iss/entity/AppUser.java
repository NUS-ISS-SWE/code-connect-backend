package com.nus.iss.entity;

import com.fasterxml.jackson.annotation.JsonManagedReference;
import jakarta.persistence.*;
import lombok.*;


@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "app_user")
public class AppUser {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;
    private String username;
    private String password;
    private String role;
    private String email;
    private String status;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    @JsonManagedReference
    private EmployerProfile employerProfile;

    @OneToOne(mappedBy = "appUser", cascade = CascadeType.ALL)
    @JsonManagedReference
    private EmployeeProfile employeeProfile;

}
