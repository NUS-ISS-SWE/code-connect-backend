package com.nus.iss;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.Optional;
import java.util.Map;

@Service
public class ProfileService {

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public Profile createProfile(Profile profile) {
        return profileRepository.save(profile);
    }

    public List<Profile> getAllProfiles() {
        return profileRepository.findAll();
    }

    public Optional<Profile> getProfileById(Long id) {
        return profileRepository.findById(id);
    }

    public Profile updateProfile(Long id, Profile updatedProfile) {
        if (profileRepository.existsById(id)) {
            updatedProfile.setId(id);
            return profileRepository.save(updatedProfile);
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

    public void deleteProfile(Long id) {
        profileRepository.deleteById(id);
    }

    public Profile updateProfileField(Long id, Map<String, Object> updates) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            updates.forEach((key, value) -> {
                switch (key) {
                    case "fullName":
                        profile.setFullName((String) value);
                        break;
                    case "jobTitle":
                        profile.setJobTitle((String) value);
                        break;
                    case "currentCompany":
                        profile.setCurrentCompany((String) value);
                        break;
                    case "location":
                        profile.setLocation((String) value);
                        break;
                    case "email":
                        profile.setEmail((String) value);
                        break;
                    case "phone":
                        profile.setPhone((String) value);
                        break;
                    case "aboutMe":
                        profile.setAboutMe((String) value);
                        break;
                    case "programmingLanguages":
                        profile.setProgrammingLanguages((String) value);
                        break;
                    case "education":
                        profile.setEducation((String) value);
                        break;
                    case "experience":
                        profile.setExperience((String) value);
                        break;
                    default:
                        throw new IllegalArgumentException("Invalid field: " + key);
                }
            });
            return profileRepository.save(profile);
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

    public Profile uploadResume(Long id, MultipartFile file) throws IOException {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            String fileName = fileStorageService.storeResumeFile(file);
            profile.setResumeFileName(fileName);
            return profileRepository.save(profile);
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

    public Profile deleteResume(Long id) throws IOException {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            String fileName = profile.getResumeFileName();
            if (fileName != null) {
                fileStorageService.deleteResumeFile(fileName);
                profile.setResumeFileName(null);
                return profileRepository.save(profile);
            } else {
                throw new RuntimeException("Resume not found");
            }
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

    public byte[] getResume(Long id) throws IOException {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            String fileName = profile.getResumeFileName();
            if (fileName != null) {
                return fileStorageService.getResumeFile(fileName);
            } else {
                throw new RuntimeException("Resume not found");
            }
        } else {
            throw new RuntimeException("Profile not found");
        }
    }
}