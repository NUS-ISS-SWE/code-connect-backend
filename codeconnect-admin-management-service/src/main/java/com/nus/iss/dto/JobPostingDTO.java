package com.nus.iss.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.Date;

@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class JobPostingDTO {

    private Long id;
    private String companyName;
    private String companyDescription;
    private String jobTitle;
    private String jobType;
    private String jobLocation;
    private String jobDescription;
    private String requiredSkills;
    private String preferredSkills;
    private String requiredCertifications;
    private byte[] thumbnail;
    private Date postedDate;
    private String salaryRange;
    private int numberApplied;
    private String status;
}