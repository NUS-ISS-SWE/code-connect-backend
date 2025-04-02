package com.nus.iss.service;

import java.io.IOException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

import com.nus.iss.model.Profile;
import com.nus.iss.repository.ProfileRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
public class ProfileService {

    private static final Logger logger = LoggerFactory.getLogger(ProfileService.class);

    @Autowired
    private ProfileRepository profileRepository;

    @Autowired
    private FileStorageService fileStorageService;

    public Profile getProfileByUsername(String username) {
        return profileRepository.findByUsername(username)
                .orElseThrow(() -> new RuntimeException("Profile not found"));
    }


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
                        profile.setEducation((List<String>) value);
                        break;
                    case "experience":
                        profile.setExperience((List<String>) value);
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
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        if (file.isEmpty() || file.getOriginalFilename().isEmpty()) {
            if (profile.getResumeFileName() != null) {
                fileStorageService.deleteResumeFile(profile.getResumeFileName());
            }
            profile.setResume(null);
            profile.setResumeFileName(null);
        } else {
            String fileName = fileStorageService.storeResumeFile(file);
            profile.setResume("Resume uploaded");
            profile.setResumeFileName(fileName);
        }
        return profileRepository.save(profile);
    }

    public Profile deleteResume(Long id) throws IOException {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        if (profile.getResumeFileName() != null) {
            fileStorageService.deleteResumeFile(profile.getResumeFileName());
            profile.setResume(null);
            profile.setResumeFileName(null);
            return profileRepository.save(profile);
        }
        return profile;
    }

    public byte[] getResume(Long id) throws IOException {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
        if (profile.getResumeFileName() == null) {
            throw new IllegalArgumentException("Resume not found");
        }
        return fileStorageService.getResumeFile(profile.getResumeFileName());
    }

    public Profile updateProfilePicture(Long id, MultipartFile file) throws IOException {
        logger.info("Updating profile picture for profile ID: {}", id);
        String contentType = file.getContentType();
        logger.info("File content type: {}", contentType);
        if (!contentType.equals("image/png") && !contentType.equals("image/jpeg")) {
            throw new IllegalArgumentException("Only PNG and JPEG formats are supported for profile pictures");
        }

        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setProfilePicture(file.getBytes());
        logger.info("Profile picture updated for profile ID: {}", id);
        return profileRepository.save(profile);
    }

    public byte[] getProfilePicture(Long id) {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        return profile.getProfilePicture();
    }

    public Profile deleteProfilePicture(Long id) {
        logger.info("Deleting profile picture for profile ID: {}", id);
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setProfilePicture(null);
        logger.info("Profile picture deleted for profile ID: {}", id);
        return profileRepository.save(profile);
    }

    public Profile updateCertifications(Long id, List<String> certifications) {
        logger.info("Updating certifications for profile ID: {}", id);
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setCertifications(certifications);
        logger.info("Certifications updated for profile ID: {}", id);
        return profileRepository.save(profile);
    }

    public List<String> getCertifications(Long id) {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        return profile.getCertifications();
    }

    public Profile deleteCertification(Long id, String certification) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            List<String> certifications = profile.getCertifications();
            certifications.remove(certification);
            profile.setCertifications(certifications);
            return profileRepository.save(profile);
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

    public Profile addCertifications(Long id, List<String> certifications) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            List<String> existingCertifications = profile.getCertifications();
            existingCertifications.addAll(certifications);
            profile.setCertifications(existingCertifications);
            return profileRepository.save(profile);
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

    public Profile updateSkillSet(Long id, List<String> skillSet) {
        logger.info("Updating skill set for profile ID: {}", id);
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setSkillSet(skillSet);
        logger.info("Skill set updated for profile ID: {}", id);
        return profileRepository.save(profile);
    }

    public List<String> getSkillSet(Long id) {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        return profile.getSkillSet();
    }

    public Profile deleteSkill(Long id, String skill) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            List<String> skillSet = profile.getSkillSet();
            skillSet.remove(skill);
            profile.setSkillSet(skillSet);
            return profileRepository.save(profile);
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

    public Profile addSkillSet(Long id, List<String> skillSet) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            List<String> existingSkillSet = profile.getSkillSet();
            existingSkillSet.addAll(skillSet);
            profile.setSkillSet(existingSkillSet);
            return profileRepository.save(profile);
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

    public Profile updateEducation(Long id, List<String> education) {
        logger.info("Updating education for profile ID: {}", id);
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setEducation(education);
        logger.info("Education updated for profile ID: {}", id);
        return profileRepository.save(profile);
    }

    public List<String> getEducation(Long id) {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        return profile.getEducation();
    }

    public Profile deleteEducationEntry(Long id, String educationEntry) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            List<String> education = profile.getEducation();
            education.remove(educationEntry);
            profile.setEducation(education);
            return profileRepository.save(profile);
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

    public Profile addEducation(Long id, List<String> education) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            List<String> existingEducation = profile.getEducation();
            existingEducation.addAll(education);
            profile.setEducation(existingEducation);
            return profileRepository.save(profile);
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

    public Profile updateExperience(Long id, List<String> experience) {
        logger.info("Updating experience for profile ID: {}", id);
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        profile.setExperience(experience);
        logger.info("Experience updated for profile ID: {}", id);
        return profileRepository.save(profile);
    }

    public List<String> getExperience(Long id) {
        Profile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
        return profile.getExperience();
    }

    public Profile deleteExperienceEntry(Long id, String experienceEntry) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            List<String> experience = profile.getExperience();
            experience.remove(experienceEntry);
            profile.setExperience(experience);
            return profileRepository.save(profile);
        } else {
            throw new RuntimeException("Profile not found");
        }
    }

    public Profile addExperience(Long id, List<String> experience) {
        Optional<Profile> optionalProfile = profileRepository.findById(id);
        if (optionalProfile.isPresent()) {
            Profile profile = optionalProfile.get();
            List<String> existingExperience = profile.getExperience();
            existingExperience.addAll(experience);
            profile.setExperience(existingExperience);
            return profileRepository.save(profile);
        } else {
            throw new RuntimeException("Profile not found");
        }
    }
}