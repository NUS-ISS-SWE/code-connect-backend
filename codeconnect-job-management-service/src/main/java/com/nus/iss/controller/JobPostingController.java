package com.nus.iss.controller;

import com.nus.iss.model.JobPosting;
import com.nus.iss.service.JobPostingService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/jobpostings")
public class JobPostingController {

    @Autowired
    private JobPostingService jobPostingService;

    @GetMapping
    public List<JobPosting> getAllJobPostings() {
        return jobPostingService.getAllJobPostings();
    }

    @GetMapping("/{id}")
    public ResponseEntity<JobPosting> getJobPostingById(@PathVariable Long id) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting.map(ResponseEntity::ok).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping
    public JobPosting createJobPosting(@RequestBody JobPosting jobPosting) {
        return jobPostingService.createJobPosting(jobPosting);
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteJobPosting(@PathVariable Long id) {
        jobPostingService.deleteJobPosting(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/{id}/companyName")
    public ResponseEntity<String> getCompanyName(@PathVariable Long id) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting.map(jp -> ResponseEntity.ok(jp.getCompanyName())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/companyName")
    public ResponseEntity<Void> updateCompanyName(@PathVariable Long id, @RequestBody String companyName) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting.isPresent()) {
            JobPosting jp = jobPosting.get();
            jp.setCompanyName(companyName);
            jobPostingService.createJobPosting(jp);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/companyDescription")
    public ResponseEntity<String> getCompanyDescription(@PathVariable Long id) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting.map(jp -> ResponseEntity.ok(jp.getCompanyDescription())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/companyDescription")
    public ResponseEntity<Void> updateCompanyDescription(@PathVariable Long id, @RequestBody String companyDescription) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting.isPresent()) {
            JobPosting jp = jobPosting.get();
            jp.setCompanyDescription(companyDescription);
            jobPostingService.createJobPosting(jp);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/jobTitle")
    public ResponseEntity<String> getJobTitle(@PathVariable Long id) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting.map(jp -> ResponseEntity.ok(jp.getJobTitle())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/jobTitle")
    public ResponseEntity<Void> updateJobTitle(@PathVariable Long id, @RequestBody String jobTitle) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting.isPresent()) {
            JobPosting jp = jobPosting.get();
            jp.setJobTitle(jobTitle);
            jobPostingService.createJobPosting(jp);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/jobType")
    public ResponseEntity<String> getJobType(@PathVariable Long id) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting.map(jp -> ResponseEntity.ok(jp.getJobType())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/jobType")
    public ResponseEntity<Void> updateJobType(@PathVariable Long id, @RequestBody String jobType) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting.isPresent()) {
            JobPosting jp = jobPosting.get();
            jp.setJobType(jobType);
            jobPostingService.createJobPosting(jp);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/jobLocation")
    public ResponseEntity<String> getJobLocation(@PathVariable Long id) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting.map(jp -> ResponseEntity.ok(jp.getJobLocation())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/jobLocation")
    public ResponseEntity<Void> updateJobLocation(@PathVariable Long id, @RequestBody String jobLocation) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting.isPresent()) {
            JobPosting jp = jobPosting.get();
            jp.setJobLocation(jobLocation);
            jobPostingService.createJobPosting(jp);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/jobDescription")
    public ResponseEntity<String> getJobDescription(@PathVariable Long id) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting.map(jp -> ResponseEntity.ok(jp.getJobDescription())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/jobDescription")
    public ResponseEntity<Void> updateJobDescription(@PathVariable Long id, @RequestBody String jobDescription) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting.isPresent()) {
            JobPosting jp = jobPosting.get();
            jp.setJobDescription(jobDescription);
            jobPostingService.createJobPosting(jp);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/requiredSkills")
    public ResponseEntity<String> getRequiredSkills(@PathVariable Long id) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting.map(jp -> ResponseEntity.ok(jp.getRequiredSkills())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/requiredSkills")
    public ResponseEntity<Void> updateRequiredSkills(@PathVariable Long id, @RequestBody String requiredSkills) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting.isPresent()) {
            JobPosting jp = jobPosting.get();
            jp.setRequiredSkills(requiredSkills);
            jobPostingService.createJobPosting(jp);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/preferredSkills")
    public ResponseEntity<String> getPreferredSkills(@PathVariable Long id) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting.map(jp -> ResponseEntity.ok(jp.getPreferredSkills())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/preferredSkills")
    public ResponseEntity<Void> updatePreferredSkills(@PathVariable Long id, @RequestBody String preferredSkills) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting.isPresent()) {
            JobPosting jp = jobPosting.get();
            jp.setPreferredSkills(preferredSkills);
            jobPostingService.createJobPosting(jp);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/requiredCertifications")
    public ResponseEntity<String> getRequiredCertifications(@PathVariable Long id) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting.map(jp -> ResponseEntity.ok(jp.getRequiredCertifications())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/requiredCertifications")
    public ResponseEntity<Void> updateRequiredCertifications(@PathVariable Long id, @RequestBody String requiredCertifications) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting.isPresent()) {
            JobPosting jp = jobPosting.get();
            jp.setRequiredCertifications(requiredCertifications);
            jobPostingService.createJobPosting(jp);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/thumbnail")
    public ResponseEntity<byte[]> getThumbnail(@PathVariable Long id) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting.map(jp -> ResponseEntity.ok(jp.getThumbnail())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/thumbnail")
    public ResponseEntity<Void> updateThumbnail(@PathVariable Long id, @RequestParam("thumbnail") MultipartFile thumbnail) throws IOException {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting.isPresent()) {
            JobPosting jp = jobPosting.get();
            if (thumbnail.isEmpty()) {
                jp.setThumbnail(null);
            } else {
                jp.setThumbnail(thumbnail.getBytes());
            }
            jobPostingService.createJobPosting(jp);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/postedDate")
    public ResponseEntity<Date> getPostedDate(@PathVariable Long id) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting.map(jp -> ResponseEntity.ok(jp.getPostedDate())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/postedDate")
    public ResponseEntity<Void> updatePostedDate(@PathVariable Long id, @RequestBody Date postedDate) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting.isPresent()) {
            JobPosting jp = jobPosting.get();
            jp.setPostedDate(postedDate);
            jobPostingService.createJobPosting(jp);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }

    @GetMapping("/{id}/salaryRange")
    public ResponseEntity<String> getSalaryRange(@PathVariable Long id) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        return jobPosting.map(jp -> ResponseEntity.ok(jp.getSalaryRange())).orElseGet(() -> ResponseEntity.notFound().build());
    }

    @PostMapping("/{id}/salaryRange")
    public ResponseEntity<Void> updateSalaryRange(@PathVariable Long id, @RequestBody String salaryRange) {
        Optional<JobPosting> jobPosting = jobPostingService.getJobPostingById(id);
        if (jobPosting.isPresent()) {
            JobPosting jp = jobPosting.get();
            jp.setSalaryRange(salaryRange);
            jobPostingService.createJobPosting(jp);
            return ResponseEntity.noContent().build();
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}