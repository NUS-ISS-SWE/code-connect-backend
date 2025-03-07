package com.nus.iss;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

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
        jobPosting.setTitle("Software Engineer");
        jobPosting.setDescription("Develop and maintain software applications.");
        jobPosting.setCompany("Tech Company");
        jobPosting.setLocation("New York");
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