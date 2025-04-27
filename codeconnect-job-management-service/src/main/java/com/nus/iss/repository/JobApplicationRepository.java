package com.nus.iss.repository;

import com.nus.iss.model.JobApplication;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface JobApplicationRepository extends JpaRepository<JobApplication, Long> {
    Optional<JobApplication> findByJobPostingIdAndApplicantName(Long jobPostingId, String applicantName);
}