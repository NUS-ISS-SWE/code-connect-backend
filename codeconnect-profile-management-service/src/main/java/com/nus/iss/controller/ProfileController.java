package com.nus.iss.controller;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.nus.iss.model.Profile;
import com.nus.iss.service.ProfileService;
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
public class ProfileController {

    @Autowired
    private ProfileService profileService;

    @Value("${cdcnt.security.jwt.secret}")
    private String secret;

    @GetMapping("/current")
    public Profile getCurrentUserProfile(@RequestHeader("Authorization") String authHeader) {
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
    public Profile createProfile(@RequestBody Profile profile) {
        return profileService.createProfile(profile);
    }

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profileService.getAllProfiles();
    }

    @GetMapping("/{id}")
    public Optional<Profile> getProfileById(@PathVariable Long id) {
        return profileService.getProfileById(id);
    }

    @PutMapping("/{id}")
    public Profile updateProfile(@PathVariable Long id, @RequestBody Profile profile) {
        return profileService.updateProfile(id, profile);
    }

    @DeleteMapping("/{id}")
    public void deleteProfile(@PathVariable Long id) {
        profileService.deleteProfile(id);
    }

    @PatchMapping("/{id}")
    public Profile updateProfileField(@PathVariable Long id, @RequestBody Map<String, Object> updates) {
        return profileService.updateProfileField(id, updates);
    }

    @PostMapping("/{id}/uploadResume")
    public Profile uploadResume(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        return profileService.uploadResume(id, file);
    }

    @DeleteMapping("/{id}/deleteResume")
    public Profile deleteResume(@PathVariable Long id) throws IOException {
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
    public Profile updateProfilePicture(@PathVariable Long id, @RequestParam("file") MultipartFile file) throws IOException {
        return profileService.updateProfilePicture(id, file);
    }

    @DeleteMapping("/{id}/profilePicture")
    public Profile deleteProfilePicture(@PathVariable Long id) {
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
    public Profile updateCertifications(@PathVariable Long id, @RequestBody List<String> certifications) {
        return profileService.updateCertifications(id, certifications);
    }

    @GetMapping("/{id}/certifications")
    public List<String> getCertifications(@PathVariable Long id) {
        return profileService.getCertifications(id);
    }

    @DeleteMapping("/{id}/certifications")
    public Profile deleteAllCertifications(@PathVariable Long id) {
        return profileService.updateCertifications(id, List.of());
    }

    @DeleteMapping("/{id}/certifications/{certification}")
    public Profile deleteCertification(@PathVariable Long id, @PathVariable String certification) {
        return profileService.deleteCertification(id, certification);
    }

    @PostMapping("/{id}/certifications/add")
    public Profile addCertifications(@PathVariable Long id, @RequestBody List<String> certifications) {
        return profileService.addCertifications(id, certifications);
    }

    @PostMapping("/{id}/skillSet")
    public Profile updateSkillSet(@PathVariable Long id, @RequestBody List<String> skillSet) {
        return profileService.updateSkillSet(id, skillSet);
    }

    @GetMapping("/{id}/skillSet")
    public List<String> getSkillSet(@PathVariable Long id) {
        return profileService.getSkillSet(id);
    }

    @DeleteMapping("/{id}/skillSet")
    public Profile deleteAllSkillSet(@PathVariable Long id) {
        return profileService.updateSkillSet(id, List.of());
    }

    @DeleteMapping("/{id}/skillSet/{skill}")
    public Profile deleteSkill(@PathVariable Long id, @PathVariable String skill) {
        return profileService.deleteSkill(id, skill);
    }

    @PostMapping("/{id}/skillSet/add")
    public Profile addSkillSet(@PathVariable Long id, @RequestBody List<String> skillSet) {
        return profileService.addSkillSet(id, skillSet);
    }

    @PostMapping("/{id}/education")
    public Profile updateEducation(@PathVariable Long id, @RequestBody List<String> education) {
        return profileService.updateEducation(id, education);
    }

    @GetMapping("/{id}/education")
    public List<String> getEducation(@PathVariable Long id) {
        return profileService.getEducation(id);
    }

    @DeleteMapping("/{id}/education")
    public Profile deleteAllEducation(@PathVariable Long id) {
        return profileService.updateEducation(id, List.of());
    }

    @DeleteMapping("/{id}/education/{educationEntry}")
    public Profile deleteEducationEntry(@PathVariable Long id, @PathVariable String educationEntry) {
        return profileService.deleteEducationEntry(id, educationEntry);
    }

    @PostMapping("/{id}/education/add")
    public Profile addEducation(@PathVariable Long id, @RequestBody List<String> education) {
        return profileService.addEducation(id, education);
    }

    @PostMapping("/{id}/experience")
    public Profile updateExperience(@PathVariable Long id, @RequestBody List<String> experience) {
        return profileService.updateExperience(id, experience);
    }

    @GetMapping("/{id}/experience")
    public List<String> getExperience(@PathVariable Long id) {
        return profileService.getExperience(id);
    }

    @DeleteMapping("/{id}/experience")
    public Profile deleteAllExperience(@PathVariable Long id) {
        return profileService.updateExperience(id, List.of());
    }

    @DeleteMapping("/{id}/experience/{experienceEntry}")
    public Profile deleteExperienceEntry(@PathVariable Long id, @PathVariable String experienceEntry) {
        return profileService.deleteExperienceEntry(id, experienceEntry);
    }

    @PostMapping("/{id}/experience/add")
    public Profile addExperience(@PathVariable Long id, @RequestBody List<String> experience) {
        return profileService.addExperience(id, experience);
    }
}