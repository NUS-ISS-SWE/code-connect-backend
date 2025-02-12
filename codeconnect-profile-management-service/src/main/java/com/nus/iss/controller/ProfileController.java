package com.nus.iss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.core.io.ByteArrayResource;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@CrossOrigin(origins = "http://localhost:5173")
@RestController
@RequestMapping("/profiles")
public class ProfileController {

    @Autowired
    private ProfileService profileService;

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
}