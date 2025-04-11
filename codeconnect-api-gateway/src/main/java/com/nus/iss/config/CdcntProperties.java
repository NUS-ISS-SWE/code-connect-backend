package com.nus.iss.config;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@ConfigurationProperties(prefix = "cdcnt")
@NoArgsConstructor
@AllArgsConstructor
@Data
public class CdcntProperties {
    public String userService;
    public String adminService;
    public String jobService;
    public String interviewPrepService;

}
