package com.nus.iss.entity;

import jakarta.persistence.*;
import lombok.*;

@NoArgsConstructor
@AllArgsConstructor
@Getter
@Setter
@Builder
@Entity
@Table(name = "app_user_media")
public class AppUserMedia {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String profilePicture;
    private String profilePictureFileName;

    private String resumeContent;
    private String resumeFileName;

    @OneToOne
    @JoinColumn(name = "app_user_id", unique = true)
    private AppUser appUser;
}
