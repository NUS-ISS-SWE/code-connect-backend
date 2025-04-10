package com.nus.iss.config;

import lombok.*;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cdcnt")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CdcntProperties {
    public Services services;

    @Getter
    @Setter
    public static class Services {
        private UserService userService;


        @Getter
        @Setter
        public static class UserService {
            private String url;
            private String getAllEmployerProfiles;
            private String reviewEmployerProfile;

        }
    }
}
