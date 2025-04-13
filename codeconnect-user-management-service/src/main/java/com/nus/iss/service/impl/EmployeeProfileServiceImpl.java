//package com.nus.iss.service.impl;
//
//import com.nus.iss.entity.EmployeeProfile;
//import com.nus.iss.repository.EmployeeProfileRepository;
//import com.nus.iss.service.EmployeeProfileService;
//import lombok.extern.slf4j.Slf4j;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Service;
//import org.springframework.web.multipart.MultipartFile;
//
//import java.io.IOException;
//import java.util.List;
//import java.util.Map;
//import java.util.Optional;
//
//@Service
//@Slf4j
//public class EmployeeProfileServiceImpl implements EmployeeProfileService {
//
//    private final EmployeeProfileRepository profileRepository;
//    private final FileStorageService fileStorageService;
//
//    @Autowired
//    public EmployeeProfileServiceImpl(EmployeeProfileRepository profileRepository, FileStorageService fileStorageService) {
//        this.profileRepository = profileRepository;
//        this.fileStorageService = fileStorageService;
//    }
//
////    public EmployeeProfile getProfileByUsername(String username) {
////        return profileRepository.findByUsername(username)
////                .orElseThrow(() -> new RuntimeException("Profile not found"));
////    }
//
//
//    public EmployeeProfile createProfile(EmployeeProfile profile) {
//        return profileRepository.save(profile);
//    }
//
//    public List<EmployeeProfile> getAllProfiles() {
//        return profileRepository.findAll();
//    }
//
//    public Optional<EmployeeProfile> getProfileById(Long id) {
//        return profileRepository.findById(id);
//    }
//
//    public EmployeeProfile updateProfile(Long id, EmployeeProfile updatedProfile) {
//        if (profileRepository.existsById(id)) {
//            updatedProfile.setId(id);
//            return profileRepository.save(updatedProfile);
//        } else {
//            throw new RuntimeException("Profile not found");
//        }
//    }
//
//    public void deleteProfile(Long id) {
//        profileRepository.deleteById(id);
//    }
//
//    public EmployeeProfile updateProfileField(Long id, Map<String, Object> updates) {
//        Optional<EmployeeProfile> optionalProfile = profileRepository.findById(id);
//        if (optionalProfile.isPresent()) {
//            EmployeeProfile profile = optionalProfile.get();
//            updates.forEach((key, value) -> {
//                switch (key) {
//                    case "fullName":
//                        profile.setFullName((String) value);
//                        break;
//                    case "jobTitle":
//                        profile.setJobTitle((String) value);
//                        break;
//                    case "currentCompany":
//                        profile.setCurrentCompany((String) value);
//                        break;
//                    case "location":
//                        profile.setLocation((String) value);
//                        break;
//                    case "phone":
//                        profile.setPhone((String) value);
//                        break;
//                    case "aboutMe":
//                        profile.setAboutMe((String) value);
//                        break;
//                    case "programmingLanguages":
//                        profile.setProgrammingLanguage((String) value);
//                        break;
//                    case "education":
//                        profile.setEducation((List<String>) value);
//                        break;
//                    case "experience":
//                        profile.setExperience((List<String>) value);
//                        break;
//                    default:
//                        throw new IllegalArgumentException("Invalid field: " + key);
//                }
//            });
//            return profileRepository.save(profile);
//        } else {
//            throw new RuntimeException("Profile not found");
//        }
//    }
//
//    public EmployeeProfile uploadResume(Long id, MultipartFile file) throws IOException {
//        EmployeeProfile profile = profileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
//        if (file.isEmpty() || file.getOriginalFilename().isEmpty()) {
//            if (profile.getResumeFileName() != null) {
//                fileStorageService.deleteResumeFile(profile.getResumeFileName());
//            }
//            profile.setResume(null);
//            profile.setResumeFileName(null);
//        } else {
//            String fileName = fileStorageService.storeResumeFile(file);
//            profile.setResume("Resume uploaded");
//            profile.setResumeFileName(fileName);
//        }
//        return profileRepository.save(profile);
//    }
//
//    public EmployeeProfile deleteResume(Long id) throws IOException {
//        EmployeeProfile profile = profileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
//        if (profile.getResumeFileName() != null) {
//            fileStorageService.deleteResumeFile(profile.getResumeFileName());
//            profile.setResume(null);
//            profile.setResumeFileName(null);
//            return profileRepository.save(profile);
//        }
//        return profile;
//    }
//
//    public byte[] getResume(Long id) throws IOException {
//        EmployeeProfile profile = profileRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Profile not found"));
//        if (profile.getResumeFileName() == null) {
//            throw new IllegalArgumentException("Resume not found");
//        }
//        return fileStorageService.getResumeFile(profile.getResumeFileName());
//    }
//
//    public EmployeeProfile updateProfilePicture(Long id, MultipartFile file) throws IOException {
//        log.info("Updating profile picture for profile ID: {}", id);
//        String contentType = file.getContentType();
//        log.info("File content type: {}", contentType);
//        if (!contentType.equals("image/png") && !contentType.equals("image/jpeg")) {
//            throw new IllegalArgumentException("Only PNG and JPEG formats are supported for profile pictures");
//        }
//
//        EmployeeProfile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
//        profile.setProfilePicture(file.getBytes());
//        log.info("Profile picture updated for profile ID: {}", id);
//        return profileRepository.save(profile);
//    }
//
//    public byte[] getProfilePicture(Long id) {
//        EmployeeProfile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
//        return profile.getProfilePicture();
//    }
//
//    public EmployeeProfile deleteProfilePicture(Long id) {
//        log.info("Deleting profile picture for profile ID: {}", id);
//        EmployeeProfile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
//        profile.setProfilePicture(null);
//        log.info("Profile picture deleted for profile ID: {}", id);
//        return profileRepository.save(profile);
//    }
//
//    public EmployeeProfile updateCertifications(Long id, List<String> certifications) {
//        log.info("Updating certifications for profile ID: {}", id);
//        EmployeeProfile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
//        profile.setCertification(certifications);
//        log.info("Certifications updated for profile ID: {}", id);
//        return profileRepository.save(profile);
//    }
//
//    public List<String> getCertifications(Long id) {
//        EmployeeProfile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
//        return profile.getCertification();
//    }
//
//    public EmployeeProfile deleteCertification(Long id, String certification) {
//        Optional<EmployeeProfile> optionalProfile = profileRepository.findById(id);
//        if (optionalProfile.isPresent()) {
//            EmployeeProfile profile = optionalProfile.get();
//            List<String> certifications = profile.getCertification();
//            certifications.remove(certification);
//            profile.setCertification(certifications);
//            return profileRepository.save(profile);
//        } else {
//            throw new RuntimeException("Profile not found");
//        }
//    }
//
//    public EmployeeProfile addCertifications(Long id, List<String> certifications) {
//        Optional<EmployeeProfile> optionalProfile = profileRepository.findById(id);
//        if (optionalProfile.isPresent()) {
//            EmployeeProfile profile = optionalProfile.get();
//            List<String> existingCertifications = profile.getCertification();
//            existingCertifications.addAll(certifications);
//            profile.setCertification(existingCertifications);
//            return profileRepository.save(profile);
//        } else {
//            throw new RuntimeException("Profile not found");
//        }
//    }
//
//    public EmployeeProfile updateSkillSet(Long id, List<String> skillSet) {
//        log.info("Updating skill set for profile ID: {}", id);
//        EmployeeProfile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
//        profile.setSkillSet(skillSet);
//        log.info("Skill set updated for profile ID: {}", id);
//        return profileRepository.save(profile);
//    }
//
//    public List<String> getSkillSet(Long id) {
//        EmployeeProfile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
//        return profile.getSkillSet();
//    }
//
//    public EmployeeProfile deleteSkill(Long id, String skill) {
//        Optional<EmployeeProfile> optionalProfile = profileRepository.findById(id);
//        if (optionalProfile.isPresent()) {
//            EmployeeProfile profile = optionalProfile.get();
//            List<String> skillSet = profile.getSkillSet();
//            skillSet.remove(skill);
//            profile.setSkillSet(skillSet);
//            return profileRepository.save(profile);
//        } else {
//            throw new RuntimeException("Profile not found");
//        }
//    }
//
//    public EmployeeProfile addSkillSet(Long id, List<String> skillSet) {
//        Optional<EmployeeProfile> optionalProfile = profileRepository.findById(id);
//        if (optionalProfile.isPresent()) {
//            EmployeeProfile profile = optionalProfile.get();
//            List<String> existingSkillSet = profile.getSkillSet();
//            existingSkillSet.addAll(skillSet);
//            profile.setSkillSet(existingSkillSet);
//            return profileRepository.save(profile);
//        } else {
//            throw new RuntimeException("Profile not found");
//        }
//    }
//
//    public EmployeeProfile updateEducation(Long id, List<String> education) {
//        log.info("Updating education for profile ID: {}", id);
//        EmployeeProfile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
//        profile.setEducation(education);
//        log.info("Education updated for profile ID: {}", id);
//        return profileRepository.save(profile);
//    }
//
//    public List<String> getEducation(Long id) {
//        EmployeeProfile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
//        return profile.getEducation();
//    }
//
//    public EmployeeProfile deleteEducationEntry(Long id, String educationEntry) {
//        Optional<EmployeeProfile> optionalProfile = profileRepository.findById(id);
//        if (optionalProfile.isPresent()) {
//            EmployeeProfile profile = optionalProfile.get();
//            List<String> education = profile.getEducation();
//            education.remove(educationEntry);
//            profile.setEducation(education);
//            return profileRepository.save(profile);
//        } else {
//            throw new RuntimeException("Profile not found");
//        }
//    }
//
//    public EmployeeProfile addEducation(Long id, List<String> education) {
//        Optional<EmployeeProfile> optionalProfile = profileRepository.findById(id);
//        if (optionalProfile.isPresent()) {
//            EmployeeProfile profile = optionalProfile.get();
//            List<String> existingEducation = profile.getEducation();
//            existingEducation.addAll(education);
//            profile.setEducation(existingEducation);
//            return profileRepository.save(profile);
//        } else {
//            throw new RuntimeException("Profile not found");
//        }
//    }
//
//    public EmployeeProfile updateExperience(Long id, List<String> experience) {
//        log.info("Updating experience for profile ID: {}", id);
//        EmployeeProfile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
//        profile.setExperience(experience);
//        log.info("Experience updated for profile ID: {}", id);
//        return profileRepository.save(profile);
//    }
//
//    public List<String> getExperience(Long id) {
//        EmployeeProfile profile = profileRepository.findById(id).orElseThrow(() -> new RuntimeException("Profile not found"));
//        return profile.getExperience();
//    }
//
//    public EmployeeProfile deleteExperienceEntry(Long id, String experienceEntry) {
//        Optional<EmployeeProfile> optionalProfile = profileRepository.findById(id);
//        if (optionalProfile.isPresent()) {
//            EmployeeProfile profile = optionalProfile.get();
//            List<String> experience = profile.getExperience();
//            experience.remove(experienceEntry);
//            profile.setExperience(experience);
//            return profileRepository.save(profile);
//        } else {
//            throw new RuntimeException("Profile not found");
//        }
//    }
//
//    public EmployeeProfile addExperience(Long id, List<String> experience) {
//        Optional<EmployeeProfile> optionalProfile = profileRepository.findById(id);
//        if (optionalProfile.isPresent()) {
//            EmployeeProfile profile = optionalProfile.get();
//            List<String> existingExperience = profile.getExperience();
//            existingExperience.addAll(experience);
//            profile.setExperience(existingExperience);
//            return profileRepository.save(profile);
//        } else {
//            throw new RuntimeException("Profile not found");
//        }
//    }
//}
