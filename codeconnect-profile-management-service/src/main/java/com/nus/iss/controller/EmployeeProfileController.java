package com.nus.iss.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.nus.iss.model.EmployeeProfile;
import com.nus.iss.service.EmployeeProfileService;
import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.security.Keys;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.multipart.MultipartFile;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/profiles")
public class EmployeeProfileController {

    @Autowired
    private EmployeeProfileService profileService;

    @Value("${cdcnt.security.jwt.secret}")
    private String secret;

    @GetMapping("/current")
    public EmployeeProfile getCurrentUserProfile(@RequestHeader("Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Invalid token");
        }

        String jwt = authHeader.substring(7); // Remove "Bearer " prefix
        String username = extractUsername(jwt);
        return profileService.getProfileByUsername(username);
    }

    private String extractUsername(String token) {
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(Keys.hmacShaKeyFor(secret.getBytes()))
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    @PostMapping
    public EmployeeProfile createProfile(@RequestBody EmployeeProfile profile) {
        return profileService.createProfile(profile);
    }

    @GetMapping
    public List<EmployeeProfile> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public Optional<EmployeeProfile> getProfileById(@PathVariable Long id) {
        return profileService.getProfileById(id);
    }

    @PutMapping("/{id}")
    public EmployeeProfile updateProfile(@PathVariable Long id, @RequestBody EmployeeProfile profile) {
        return profileService.updateProfile(id, profile);
    }

    @DeleteMapping("/{id}")
    public void deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
    }

    @PatchMapping("/{id}")
    public EmployeeProfile updateProfileField(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return profileService.updateProfileField(id, updates);
    }

    @PostMapping("/{id}/uploadResume")
    public EmployeeProfile uploadResume(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        return profileService.uploadResume(id, file);
    }

    @DeleteMapping("/{id}/deleteResume")
    public EmployeeProfile deleteResume(@PathVariable Long id) throws IOException {
        return profileService.deleteResume(id);
    }

    @GetMapping("/{id}/resume")
    public ResponseEntity<ByteArrayResource> getResume(@PathVariable Long id) throws IOException {
        byte[] data = profileService.getResume(id);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"resume.pdf\"")
                .contentLength(data.length)
                .contentType(org.springframework.http.MediaType.APPLICATION_OCTET_STREAM)
                .body(resource);
    }

    @PostMapping("/{id}/profilePicture")
    public EmployeeProfile updateProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        return profileService.updateProfilePicture(id, file);
    }

    @DeleteMapping("/{id}/profilePicture")
    public EmployeeProfile deleteProfilePicture(@PathVariable Long id) {
        return profileService.deleteProfilePicture(id);
    }

    @GetMapping("/{id}/profilePicture")
    public ResponseEntity<ByteArrayResource> getProfilePicture(@PathVariable Long id) {
        byte[] data = profileService.getProfilePicture(id);
        ByteArrayResource resource = new ByteArrayResource(data);

        return ResponseEntity.ok()
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"profilePicture.png\"")
                .contentLength(data.length)
                .contentType(org.springframework.http.MediaType.IMAGE_PNG)
                .body(resource);
    }

    @PostMapping("/{id}/certifications")
    public EmployeeProfile updateCertifications(@PathVariable Long id, @RequestBody List<String> certifications) {
        return profileService.updateCertifications(id, certifications);
    }

    @GetMapping("/{id}/certifications")
    public List<String> getCertifications(@PathVariable Long id) {
        return profileService.getCertifications(id);
    }

    @DeleteMapping("/{id}/certifications")
    public EmployeeProfile deleteAllCertifications(@PathVariable Long id) {
        return profileService.updateCertifications(id, List.of());
    }

    @DeleteMapping("/{id}/certifications/{certification}")
    public EmployeeProfile deleteCertification(@PathVariable Long id, @PathVariable String certification) {
        return profileService.deleteCertification(id, certification);
    }

    @PostMapping("/{id}/certifications/add")
    public EmployeeProfile addCertifications(@PathVariable Long id, @RequestBody List<String> certifications) {
        return profileService.addCertifications(id, certifications);
    }

    @PostMapping("/{id}/skillSet")
    public EmployeeProfile updateSkillSet(@PathVariable Long id, @RequestBody List<String> skillSet) {
        return profileService.updateSkillSet(id, skillSet);
    }

    @GetMapping("/{id}/skillSet")
    public List<String> getSkillSet(@PathVariable Long id) {
        return profileService.getSkillSet(id);
    }

    @DeleteMapping("/{id}/skillSet")
    public EmployeeProfile deleteAllSkillSet(@PathVariable Long id) {
        return profileService.updateSkillSet(id, List.of());
    }

    @DeleteMapping("/{id}/skillSet/{skill}")
    public EmployeeProfile deleteSkill(@PathVariable Long id, @PathVariable String skill) {
        return profileService.deleteSkill(id, skill);
    }

    @PostMapping("/{id}/skillSet/add")
    public EmployeeProfile addSkillSet(@PathVariable Long id, @RequestBody List<String> skillSet) {
        return profileService.addSkillSet(id, skillSet);
    }

    @PostMapping("/{id}/education")
    public EmployeeProfile updateEducation(@PathVariable Long id, @RequestBody List<String> education) {
        return profileService.updateEducation(id, education);
    }

    @GetMapping("/{id}/education")
    public List<String> getEducation(@PathVariable Long id) {
        return profileService.getEducation(id);
    }

    @DeleteMapping("/{id}/education")
    public EmployeeProfile deleteAllEducation(@PathVariable Long id) {
        return profileService.updateEducation(id, List.of());
    }

    @DeleteMapping("/{id}/education/{educationEntry}")
    public EmployeeProfile deleteEducationEntry(@PathVariable Long id, @PathVariable String educationEntry) {
        return profileService.deleteEducationEntry(id, educationEntry);
    }

    @PostMapping("/{id}/education/add")
    public EmployeeProfile addEducation(@PathVariable Long id, @RequestBody List<String> education) {
        return profileService.addEducation(id, education);
    }

    @PostMapping("/{id}/experience")
    public EmployeeProfile updateExperience(@PathVariable Long id, @RequestBody List<String> experience) {
        return profileService.updateExperience(id, experience);
    }

    @GetMapping("/{id}/experience")
    public List<String> getExperience(@PathVariable Long id) {
        return profileService.getExperience(id);
    }

    @DeleteMapping("/{id}/experience")
    public EmployeeProfile deleteAllExperience(@PathVariable Long id) {
        return profileService.updateExperience(id, List.of());
    }

    @DeleteMapping("/{id}/experience/{experienceEntry}")
    public EmployeeProfile deleteExperienceEntry(@PathVariable Long id, @PathVariable String experienceEntry) {
        return profileService.deleteExperienceEntry(id, experienceEntry);
    }

    @PostMapping("/{id}/experience/add")
    public EmployeeProfile addExperience(@PathVariable Long id, @RequestBody List<String> experience) {
        return profileService.addExperience(id, experience);
    }
}