package com.nus.iss;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

class JobPostingControllerTest {

    @Mock
    private JobPostingService jobPostingService;

    @InjectMocks
    private JobPostingController jobPostingController;

    private MockMvc mockMvc;

    private JobPosting jobPosting;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(jobPostingController).build();
        jobPosting = new JobPosting();
        jobPosting.setId(1L);
        jobPosting.setCompanyName("Tech Company");
        jobPosting.setCompanyDescription("A leading tech company");
        jobPosting.setJobTitle("Software Engineer");
        jobPosting.setJobType("Full-time");
        jobPosting.setJobLocation("New York");
        jobPosting.setJobDescription("Develop and maintain software applications.");
        jobPosting.setRequiredSkills("Java, Spring Boot");
        jobPosting.setPreferredSkills("React, Docker");
        jobPosting.setRequiredCertifications("AWS Certified Developer");
        jobPosting.setThumbnail("thumbnail.png");
        jobPosting.setPostedDate(new Date());
        jobPosting.setSalaryRange("$80,000 - $120,000");
    }

    @Test
    void testGetAllJobPostings() throws Exception {
        List<JobPosting> jobPostings = Arrays.asList(jobPosting);
        when(jobPostingService.getAllJobPostings()).thenReturn(jobPostings);

        mockMvc.perform(get("/jobpostings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].companyName").value("Tech Company"));
    }

    @Test
    void testGetJobPostingById() throws Exception {
        when(jobPostingService.getJobPostingById(1L)).thenReturn(Optional.of(jobPosting));

        mockMvc.perform(get("/jobpostings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.companyName").value("Tech Company"));
    }

    @Test
    void testCreateJobPosting() throws Exception {
        when(jobPostingService.createJobPosting(any(JobPosting.class))).thenReturn(jobPosting);

        mockMvc.perform(post("/jobpostings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"companyName\": \"Tech Company\", \"companyDescription\": \"A leading tech company\", \"jobTitle\": \"Software Engineer\", \"jobType\": \"Full-time\", \"jobLocation\": \"New York\", \"jobDescription\": \"Develop and maintain software applications.\", \"requiredSkills\": \"Java, Spring Boot\", \"preferredSkills\": \"React, Docker\", \"requiredCertifications\": \"AWS Certified Developer\", \"thumbnail\": \"thumbnail.png\", \"postedDate\": \"2023-10-01\", \"salaryRange\": \"$80,000 - $120,000\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.companyName").value("Tech Company"));
    }

    @Test
    void testDeleteJobPosting() throws Exception {
        doNothing().when(jobPostingService).deleteJobPosting(1L);

        mockMvc.perform(delete("/jobpostings/1"))
                .andExpect(status().isNoContent());
    }
}