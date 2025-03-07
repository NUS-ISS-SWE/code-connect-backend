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
        jobPosting.setTitle("Software Engineer");
        jobPosting.setDescription("Develop and maintain software applications.");
        jobPosting.setCompany("Tech Company");
        jobPosting.setLocation("New York");
    }

    @Test
    void testGetAllJobPostings() throws Exception {
        List<JobPosting> jobPostings = Arrays.asList(jobPosting);
        when(jobPostingService.getAllJobPostings()).thenReturn(jobPostings);

        mockMvc.perform(get("/jobpostings"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].title").value("Software Engineer"));
    }

    @Test
    void testGetJobPostingById() throws Exception {
        when(jobPostingService.getJobPostingById(1L)).thenReturn(Optional.of(jobPosting));

        mockMvc.perform(get("/jobpostings/1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Software Engineer"));
    }

    @Test
    void testCreateJobPosting() throws Exception {
        when(jobPostingService.createJobPosting(any(JobPosting.class))).thenReturn(jobPosting);

        mockMvc.perform(post("/jobpostings")
                .contentType(MediaType.APPLICATION_JSON)
                .content("{\"title\": \"Software Engineer\", \"description\": \"Develop and maintain software applications.\", \"company\": \"Tech Company\", \"location\": \"New York\"}"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.title").value("Software Engineer"));
    }

    @Test
    void testDeleteJobPosting() throws Exception {
        doNothing().when(jobPostingService).deleteJobPosting(1L);

        mockMvc.perform(delete("/jobpostings/1"))
                .andExpect(status().isNoContent());
    }
}