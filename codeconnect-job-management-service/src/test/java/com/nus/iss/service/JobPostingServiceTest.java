package com.nus.iss.service;

import com.nus.iss.model.JobPosting;
import com.nus.iss.repository.JobPostingRepository;
import java.util.Arrays;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;

import com.nus.iss.model.JobPosting;
import com.nus.iss.repository.JobPostingRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

class JobPostingServiceTest {

    @Mock
    private JobPostingRepository jobPostingRepository;

    @InjectMocks
    private JobPostingService jobPostingService;

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
    }

    @Test
    void testGetAllJobPostings() {
        List<JobPosting> jobPostings = Arrays.asList(jobPosting);
        when(jobPostingRepository.findAll()).thenReturn(jobPostings);

        List<JobPosting> result = jobPostingService.getAllJobPostings();
        assertEquals(1, result.size());
        assertEquals(jobPosting, result.get(0));
    }

    @Test
    void testGetJobPostingById() {
        when(jobPostingRepository.findById(1L)).thenReturn(Optional.of(jobPosting));

        Optional<JobPosting> result = jobPostingService.getJobPostingById(1L);
        assertEquals(true, result.isPresent());
        assertEquals(jobPosting, result.get());
    }

    @Test
    void testCreateJobPosting() {
        when(jobPostingRepository.save(jobPosting)).thenReturn(jobPosting);

        JobPosting result = jobPostingService.createJobPosting(jobPosting);
        assertEquals(jobPosting, result);
    }

    @Test
    void testDeleteJobPosting() {
        doNothing().when(jobPostingRepository).deleteById(1L);

        jobPostingService.deleteJobPosting(1L);
        verify(jobPostingRepository, times(1)).deleteById(1L);
    }
}