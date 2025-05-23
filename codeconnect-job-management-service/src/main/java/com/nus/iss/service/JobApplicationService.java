package com.nus.iss.service;

import com.nus.iss.repository.JobApplicationRepository;
import com.nus.iss.repository.JobPostingRepository;
import com.nus.iss.model.JobApplication;
import com.nus.iss.model.JobPosting;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class JobApplicationService {

    @Autowired
    private JobApplicationRepository jobApplicationRepository;

    @Autowired
    private JobPostingRepository jobPostingRepository;

    @Autowired
    private EmailService emailService;

    public List<JobApplication> getAllJobApplications() {
        return jobApplicationRepository.findAll();
    }

    public Optional<JobApplication> getJobApplicationById(Long id) {
        return jobApplicationRepository.findById(id);
    }

    public JobApplication createJobApplication(Long jobPostingId, JobApplication jobApplication) {
        Optional<JobPosting> jobPosting = jobPostingRepository.findById(jobPostingId);
        if (jobPosting.isPresent()) {
            jobApplication.setJobPosting(jobPosting.get());
            return jobApplicationRepository.save(jobApplication);
        } else {
            throw new RuntimeException("Job Posting not found");
        }
    }

    public void deleteJobApplication(Long id) {
        jobApplicationRepository.deleteById(id);
    }

    public void updateApplicationStatus(Long applicationId, String newStatus) {
        Optional<JobApplication> optionalApplication = jobApplicationRepository.findById(applicationId);
        if (optionalApplication.isPresent()) {
            JobApplication application = optionalApplication.get();
            JobPosting jobPosting = application.getJobPosting(); // Get associated job posting

            // Update the status
            application.setStatus(newStatus);
            jobApplicationRepository.save(application);

            // Send email notification
            String subject = "Your Job Application Status Has Changed";
            String body = "Dear " + application.getApplicantName() + ",\n\n"
                    + "Your application for the position of \"" + jobPosting.getJobTitle() + "\" at " + jobPosting.getCompanyName()
                    + " has been updated to: " + newStatus + ".\n\n"
                    + "Job Description: " + jobPosting.getJobDescription() + "\n"
                    + "Location: " + jobPosting.getJobLocation() + "\n"
                    + "Thank you for applying to " + jobPosting.getCompanyName() + ".";
            emailService.sendEmail(application.getApplicantEmail(), subject, body);
        } else {
            throw new RuntimeException("Application not found with ID: " + applicationId);
        }
    }

    public void sendInterviewInvite(Long jobPostingId, String applicantName, String interviewDate) {
        Optional<JobApplication> jobApplicationOpt = getJobApplicationByJobPostingAndApplicantName(jobPostingId, applicantName);
        if (jobApplicationOpt.isPresent()) {
            JobApplication jobApplication = jobApplicationOpt.get();

            String applicantEmail = jobApplication.getApplicantEmail();
            String jobTitle = jobApplication.getJobPosting().getJobTitle();
            String companyName = jobApplication.getJobPosting().getCompanyName();

            String subject = "Interview Invitation for " + jobTitle;
            String body = "Dear " + applicantName + ",\n\n"
                    + "You are invited for an interview for the position of " + jobTitle + ".\n"
                    + "Interview Date: " + interviewDate + "\n\n"
                    + "Best regards,\n" + companyName;

            emailService.sendEmail(applicantEmail, subject, body);
        } else {
            throw new RuntimeException("Job Application not found for Job Posting ID: " + jobPostingId + " and Applicant Name: " + applicantName);
        }
    }

    public Optional<JobApplication> getJobApplicationByJobPostingAndApplicantName(Long jobPostingId, String applicantName) {
        return jobApplicationRepository.findByJobPostingIdAndApplicantName(jobPostingId, applicantName);
    }
}