package com.nus.iss.service;

import com.nus.iss.model.JobPosting;
import com.nus.iss.repository.JobPostingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobPostingService {

    @Autowired
    private JobPostingRepository jobPostingRepository;

    public List<JobPosting> getAllJobPostings() {
        return jobPostingRepository.findAll();
    }

    public Optional<JobPosting> getJobPostingById(Long id) {
        return jobPostingRepository.findById(id);
    }

    public JobPosting createJobPosting(JobPosting jobPosting) {
        return jobPostingRepository.save(jobPosting);
    }

    public void deleteJobPosting(Long id) {
        jobPostingRepository.deleteById(id);
    }

    public JobPosting reviewJobPosting(JobPosting jobPosting) {
        Optional<JobPosting> optionalJobPosting = jobPostingRepository.findById(jobPosting.getId());
        if (optionalJobPosting.isPresent()) {
            JobPosting existingJobPosting = optionalJobPosting.get();
            existingJobPosting.setStatus("ACTIVE");
            return jobPostingRepository.save(existingJobPosting);
        } else {
            throw new RuntimeException("Job posting not found");
        }
    }
}