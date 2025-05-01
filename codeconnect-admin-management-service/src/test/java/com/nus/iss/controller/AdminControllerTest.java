package com.nus.iss.controller;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.dto.JobPostingDTO;
import com.nus.iss.service.AdminService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;

import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import static org.mockito.Mockito.doNothing;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(AdminController.class)
class AdminControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private AdminService adminService;

    private List<AppUserDTO> employerProfilesList;
    private List<AppUserDTO> employeeProfilesList;


    @BeforeEach
    void setUp() {
        employerProfilesList = new ArrayList<>();
        employerProfilesList.add(AppUserDTO.builder()
                .id(1L)
                .username("john_doe")
                .password("password123")
                .newPassword("newPassword123")
                .role("Admin")
                .email("john.doe@example.com")
                .status("Active")
                .companyName("TechCorp")
                .companyDescription("Tech company specializing in AI solutions")
                .companySize("500")
                .industry("Technology")
                .companyLocation("Singapore")
                .build());
        employeeProfilesList = new ArrayList<>();
        employeeProfilesList.add(AppUserDTO.builder()
                .id(1L)
                .username("john_doe")
                .password("password123")
                .role("ADMIN")
                .email("john.doe@example.com")
                .status("ACTIVE")
                .fullName("John Doe")
                .jobTitle("Software Developer")
                .currentCompany("Tech Inc.")
                .location("New York")
                .phone("123-456-7890")
                .aboutMe("Passionate about coding")
                .programmingLanguage("Java, Python")
                .education("BSc Computer Science")
                .experience("5 years in software development")
                .certification("Oracle Certified Java Programmer")
                .skillSet("Java, Spring, Python, SQL")
                .build());

    }

    @Test
    void getAllEmployerProfilesReturnsEmptyListWhenNoProfilesExist() throws Exception {
        when(adminService.getAllEmployerProfiles()).thenReturn(employerProfilesList);
        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/list-employer-profiles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value("1"))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("john_doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].companyName").value("TechCorp"))
                .andExpect(jsonPath("$[0].companyDescription").value("Tech company specializing in AI solutions"))
                .andExpect(jsonPath("$[0].companySize").value("500"))
                .andExpect(jsonPath("$[0].industry").value("Technology"))
                .andExpect(jsonPath("$[0].companyLocation").value("Singapore"));
    }

    @Test
    void getAllEmployeeProfiles() throws Exception {
        when(adminService.getAllEmployeeProfiles()).thenReturn(employeeProfilesList);

        mockMvc.perform(MockMvcRequestBuilders.get("/api/v1/list-employee-profiles")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.size()").value("1"))
                .andExpect(jsonPath("$[0].id").value(1L))
                .andExpect(jsonPath("$[0].username").value("john_doe"))
                .andExpect(jsonPath("$[0].email").value("john.doe@example.com"))
                .andExpect(jsonPath("$[0].fullName").value("John Doe"))
                .andExpect(jsonPath("$[0].jobTitle").value("Software Developer"))
                .andExpect(jsonPath("$[0].currentCompany").value("Tech Inc."))
                .andExpect(jsonPath("$[0].location").value("New York"))
                .andExpect(jsonPath("$[0].phone").value("123-456-7890"))
                .andExpect(jsonPath("$[0].aboutMe").value("Passionate about coding"))
                .andExpect(jsonPath("$[0].programmingLanguage").value("Java, Python"))
                .andExpect(jsonPath("$[0].education").value("BSc Computer Science"))
                .andExpect(jsonPath("$[0].experience").value("5 years in software development"))
                .andExpect(jsonPath("$[0].certification").value("Oracle Certified Java Programmer"))
                .andExpect(jsonPath("$[0].skillSet").value("Java, Spring, Python, SQL"));

    }

    @Test
    void reviewEmployerProfile() throws Exception {
        AppUserDTO reviewedProfile = AppUserDTO.builder()
                .id(1L)
                .username("john_doe")
                .status("INACTIVE")
                .build();

        when(adminService.reviewEmployerProfile(any(AppUserDTO.class))).thenReturn(reviewedProfile);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/review-employer-profiles")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "username": "john_doe"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.username").value("john_doe"))
                .andExpect(jsonPath("$.status").value("INACTIVE"));
    }

    @Test
    void deleteProfile() throws Exception {
        doNothing().when(adminService).deleteProfile(anyString());

        mockMvc.perform(MockMvcRequestBuilders.delete("/api/v1/employer-profiles")
                        .param("username", "john_doe")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNoContent());
    }

    @Test
    void reviewJobPosting() throws Exception {
        JobPostingDTO jobPostingDTO = JobPostingDTO.builder()
                .id(1L)
                .companyName("TechCorp")
                .companyDescription("Leading tech company")
                .jobTitle("Software Engineer")
                .jobType("Full-Time")
                .jobLocation("New York, NY")
                .jobDescription("Develop and maintain software applications.")
                .status("PENDING_REVIEW")
                .build();

        JobPostingDTO reviewedJobPosting = JobPostingDTO.builder()
                .id(1L)
                .companyName("TechCorp")
                .companyDescription("Leading tech company")
                .jobTitle("Software Engineer")
                .jobType("Full-Time")
                .jobLocation("New York, NY")
                .jobDescription("Develop and maintain software applications.")
                .status("APPROVED")
                .build();

        when(adminService.reviewJobPosting(any(JobPostingDTO.class))).thenReturn(reviewedJobPosting);

        mockMvc.perform(MockMvcRequestBuilders.post("/api/v1/review-job-posting")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                                {
                                    "id": 1,
                                    "companyName": "TechCorp",
                                    "companyDescription": "Leading tech company",
                                    "jobTitle": "Software Engineer",
                                    "jobType": "Full-Time",
                                    "jobLocation": "New York, NY",
                                    "jobDescription": "Develop and maintain software applications.",
                                    "status": "PENDING_REVIEW"
                                }
                                """))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.companyName").value("TechCorp"))
                .andExpect(jsonPath("$.companyDescription").value("Leading tech company"))
                .andExpect(jsonPath("$.jobTitle").value("Software Engineer"))
                .andExpect(jsonPath("$.jobType").value("Full-Time"))
                .andExpect(jsonPath("$.jobLocation").value("New York, NY"))
                .andExpect(jsonPath("$.jobDescription").value("Develop and maintain software applications."))
                .andExpect(jsonPath("$.status").value("APPROVED"));
    }
}