package com.nus.iss;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

import java.util.Date;

import static org.mockito.Mockito.when;
import static org.mockito.ArgumentMatchers.any; 
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class JobPostingControllerTest {

    @Mock
    private JobPostingService jobPostingService;

    @InjectMocks
    private JobPostingController jobPostingController;

    @Autowired
    private MockMvc mockMvc;

    private JobPosting jobPosting;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
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
        jobPosting.setThumbnail("thumbnail.png".getBytes());
        jobPosting.setPostedDate(new Date());
        jobPosting.setSalaryRange("$80,000 - $120,000");

        mockMvc = MockMvcBuilders.standaloneSetup(jobPostingController).build();
    }

    @Test
    void testCreateJobPostingEndpoint() throws Exception {
        when(jobPostingService.createJobPosting(any(JobPosting.class))).thenReturn(jobPosting);

        String jobPostingJson = "{"
                + "\"companyName\": \"Tech Company\","
                + "\"companyDescription\": \"A leading tech company\","
                + "\"jobTitle\": \"Software Engineer\","
                + "\"jobType\": \"Full-time\","
                + "\"jobLocation\": \"New York\","
                + "\"jobDescription\": \"Develop and maintain software applications.\","
                + "\"requiredSkills\": \"Java, Spring Boot\","
                + "\"preferredSkills\": \"React, Docker\","
                + "\"requiredCertifications\": \"AWS Certified Developer\","
                + "\"thumbnail\": \"\","
                + "\"postedDate\": \"2023-10-01\","
                + "\"salaryRange\": \"$80,000 - $120,000\""
                + "}";

        mockMvc.perform(post("/jobpostings")
                .contentType(MediaType.APPLICATION_JSON)
                .content(jobPostingJson))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.companyName").value("Tech Company"))
                .andExpect(jsonPath("$.jobTitle").value("Software Engineer"));
    }
}