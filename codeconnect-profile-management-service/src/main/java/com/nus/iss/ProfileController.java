package com.nus.iss;

import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/profile")
public class ProfileController {

    private List<Profile> profiles = new ArrayList<>();

    public ProfileController() {
        // Sample profiles
        Profile profile1 = new Profile();
        profile1.setId(1L);
        profile1.setName("John Doe");
        profile1.setEmail("john.doe@example.com");
        profile1.setPhone("123-456-7890");
        profile1.setAddress("123 Main St, Anytown, USA");
        profile1.setEducation("Bachelor of Science in Computer Science");
        profile1.setExperience("5 years of software development experience");

        Profile profile2 = new Profile();
        profile2.setId(2L);
        profile2.setName("Jane Smith");
        profile2.setEmail("jane.smith@example.com");
        profile2.setPhone("987-654-3210");
        profile2.setAddress("456 Elm St, Othertown, USA");
        profile2.setEducation("Master of Science in Information Technology");
        profile2.setExperience("7 years of software development experience");

        profiles.add(profile1);
        profiles.add(profile2);
    }

    @PostMapping
    public Profile createProfile(@RequestBody Profile profile) {
        profile.setId((long) (profiles.size() + 1));
        profiles.add(profile);
        return profile;
    }

    @PutMapping("/{id}")
    public Profile updateProfile(@PathVariable Long id, @RequestBody Profile updatedProfile) {
        Optional<Profile> existingProfile = profiles.stream().filter(p -> p.getId().equals(id)).findFirst();
        if (existingProfile.isPresent()) {
            Profile profile = existingProfile.get();
            profile.setName(updatedProfile.getName());
            profile.setEmail(updatedProfile.getEmail());
            profile.setPhone(updatedProfile.getPhone());
            profile.setAddress(updatedProfile.getAddress());
            profile.setEducation(updatedProfile.getEducation());
            profile.setExperience(updatedProfile.getExperience());
            return profile;
        } else {
            return null;
        }
    }

    @DeleteMapping("/{id}")
    public void deleteProfile(@PathVariable Long id) {
        profiles.removeIf(p -> p.getId().equals(id));
    }

    @GetMapping
    public List<Profile> getAllProfiles() {
        return profiles;
    }

    @GetMapping("/{id}")
    public Profile getProfileById(@PathVariable Long id) {
        Optional<Profile> profile = profiles.stream().filter(p -> p.getId().equals(id)).findFirst();
        return profile.orElse(null);
    }

    @GetMapping("/{id}/name")
    public String getNameById(@PathVariable Long id) {
        Optional<Profile> profile = profiles.stream().filter(p -> p.getId().equals(id)).findFirst();
        return profile.map(Profile::getName).orElse(null);
    }

    @GetMapping("/{id}/email")
    public String getEmailById(@PathVariable Long id) {
        Optional<Profile> profile = profiles.stream().filter(p -> p.getId().equals(id)).findFirst();
        return profile.map(Profile::getEmail).orElse(null);
    }

    @GetMapping("/{id}/phone")
    public String getPhoneById(@PathVariable Long id) {
        Optional<Profile> profile = profiles.stream().filter(p -> p.getId().equals(id)).findFirst();
        return profile.map(Profile::getPhone).orElse(null);
    }

    @GetMapping("/{id}/address")
    public String getAddressById(@PathVariable Long id) {
        Optional<Profile> profile = profiles.stream().filter(p -> p.getId().equals(id)).findFirst();
        return profile.map(Profile::getAddress).orElse(null);
    }

    @GetMapping("/{id}/education")
    public String getEducationById(@PathVariable Long id) {
        Optional<Profile> profile = profiles.stream().filter(p -> p.getId().equals(id)).findFirst();
        return profile.map(Profile::getEducation).orElse(null);
    }

}
