package com.nus.iss.service;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.dto.JobPostingDTO;

import java.util.List;

public interface AdminService {

    List<AppUserDTO> getAllEmployerProfiles();

    AppUserDTO reviewEmployerProfile(AppUserDTO appUserDTO);

    void deleteProfile(String username);

    List<AppUserDTO> getAllEmployeeProfiles();

    JobPostingDTO reviewJobPosting(JobPostingDTO jobPostingDTO);
//
//    void getListOfJobs();
//
//    void createBlogPost();
}
