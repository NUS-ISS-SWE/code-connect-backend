package com.nus.iss.controller;

import com.nus.iss.model.JobApplication;
import com.nus.iss.service.JobApplicationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jobapplications")
public class JobApplicationController {

    @Autowired
    private JobApplicationService jobApplicationService;

    @GetMapping
    public List<JobApplication> getAllJobApplications() {
        return jobApplicationService.getAllJobApplications();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobApplication> getJobApplicationById(@PathVariable Long id) {
        Optional<JobApplication> jobApplication = jobApplicationService.getJobApplicationById(id);
        return jobApplication.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{jobPostingId}")
    public JobApplication createJobApplication(@PathVariable Long jobPostingId, @RequestBody JobApplication jobApplication) {
        return jobApplicationService.createJobApplication(jobPostingId, jobApplication);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobApplication(@PathVariable Long id) {
        jobApplicationService.deleteJobApplication(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("/{id}/status")
    public void updateStatus(@PathVariable Long id, @RequestParam String status) {
        jobApplicationService.updateApplicationStatus(id, status);
    }

    @GetMapping("/byJobTitle")
    public List<JobApplication> getJobApplicationsByJobTitle(@RequestParam String jobTitle) {
        return jobApplicationService.getAllJobApplications().stream()
                .filter(ja -> ja.getJobPosting().getJobTitle().equalsIgnoreCase(jobTitle))
                .toList();
    }

    @GetMapping("/count/byJobTitle")
    public long getJobApplicationCountByJobTitle(@RequestParam String jobTitle) {
        return jobApplicationService.getAllJobApplications().stream()
                .filter(ja -> ja.getJobPosting().getJobTitle().equalsIgnoreCase(jobTitle))
                .count();
    }

    @GetMapping("/applicantNames/byJobTitle")
    public List<String> getApplicantNamesByJobTitle(@RequestParam String jobTitle) {
        return jobApplicationService.getAllJobApplications().stream()
                .filter(ja -> ja.getJobPosting().getJobTitle().equalsIgnoreCase(jobTitle))
                .map(JobApplication::getApplicantName)
                .toList();
    }

    @PostMapping("/{jobPostingId}/sendInterviewInvite")
    public ResponseEntity<String> sendInterviewInvite(
            @PathVariable Long jobPostingId,
            @RequestParam String applicantName,
            @RequestParam String interviewDate) {
        try {
            jobApplicationService.sendInterviewInvite(jobPostingId, applicantName, interviewDate);
            return ResponseEntity.ok("Interview invite sent successfully to " + applicantName);
        } catch (RuntimeException e) {
            return ResponseEntity.badRequest().body(e.getMessage());
        }
    }

}