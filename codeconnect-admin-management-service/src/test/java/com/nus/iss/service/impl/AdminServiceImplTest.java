package com.nus.iss.service.impl;

import com.nus.iss.config.CdcntProperties;
import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.dto.JobPostingDTO;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatchers;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.web.client.RestClient;

import java.net.URI;
import java.util.ArrayList;
import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminServiceImplTest {

    @InjectMocks
    private AdminServiceImpl adminService;

    @Mock
    private RestClient restClient;

    @Mock
    private RestClient.RequestHeadersUriSpec requestHeadersUriSpec;

    @Mock
    private RestClient.RequestBodyUriSpec requestBodyUriSpec;

    @Mock
    private RestClient.ResponseSpec responseSpec;

    @Mock
    private CdcntProperties cdcntProperties;

    @Mock
    private CdcntProperties.Services services;

    @Mock
    private CdcntProperties.Services.UserService userService;

    @Mock
    private CdcntProperties.Services.JobService jobService;

    private List<AppUserDTO> employerProfilesList;
    private List<AppUserDTO> employeeProfilesList;

    @BeforeEach
    void setUp() {
        employerProfilesList = new ArrayList<>();
        employerProfilesList.add(AppUserDTO.builder()
                .id(1L)
                .username("employer1")
                .password("password")
                .role("EMPLOYER")
                .email("john.doe@example.com")
                .status("ACTIVE")
                .companyName("TechCorp")
                .companyDescription("Tech company specializing in AI solutions")
                .companySize("500")
                .industry("Technology")
                .companyLocation("Singapore")
                .build());

        employerProfilesList.add(AppUserDTO.builder()
                .id(2L)
                .username("employer2")
                .password("password")
                .role("EMPLOYER")
                .email("jane.smith@example.com")
                .status("ACTIVE")
                .companyName("InnovateLtd")
                .companyDescription("Innovation in robotics")
                .companySize("200")
                .industry("Robotics")
                .companyLocation("Singapore")
                .build());
        employeeProfilesList = new ArrayList<>();
        employeeProfilesList.add(AppUserDTO.builder()
                .id(1L)
                .username("employee1")
                .password("password")
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
    void getAllEmployerProfiles() {
        when(cdcntProperties.getServices()).thenReturn(services);
        when(services.getUserService()).thenReturn(userService);
        when(userService.getUrl()).thenReturn("http://localhost:8080");
        when(userService.getGetAllEmployerProfiles()).thenReturn("/list-employer-profiles");

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8080/list-employer-profiles")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(new ParameterizedTypeReference<List<AppUserDTO>>() {
        })).thenReturn(employerProfilesList);
        List<AppUserDTO> result = adminService.getAllEmployerProfiles();
        assertEquals(employerProfilesList, result);
    }

    @Test
    void reviewEmployerProfile() {
        AppUserDTO appUserDTO = new AppUserDTO();
        appUserDTO.setUsername("testUser");
        AppUserDTO expectedResponse = new AppUserDTO();
        expectedResponse.setUsername("testUser");

        when(cdcntProperties.getServices()).thenReturn(services);
        when(services.getUserService()).thenReturn(userService);
        when(userService.getUrl()).thenReturn("http://localhost:8080");
        when(userService.getReviewEmployerProfiles()).thenReturn("/review-employer-profiles");

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("http://localhost:8080/review-employer-profiles")).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(appUserDTO)).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(new ParameterizedTypeReference<AppUserDTO>() {
        })).thenReturn(expectedResponse);

        AppUserDTO result = adminService.reviewEmployerProfile(appUserDTO);
        assertEquals(expectedResponse, result);
        verify(requestBodyUriSpec).uri("http://localhost:8080/review-employer-profiles");
        verify(requestBodyUriSpec).body(appUserDTO);
    }

    @Test
    public void testDeleteProfile() {
        when(cdcntProperties.getServices()).thenReturn(services);
        when(services.getUserService()).thenReturn(userService);
        when(userService.getUrl()).thenReturn("http://localhost:8080");
        when(userService.getDeleteEmployerProfiles()).thenReturn("/employer-profiles");

        when(restClient.delete()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri(ArgumentMatchers.any(URI.class))).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);

        adminService.deleteProfile("testUser");

        verify(requestHeadersUriSpec).uri((URI) ArgumentMatchers.argThat(uri ->
                uri.toString().equals("http://localhost:8080/employer-profiles?username=testUser")
        ));
    }

    @Test
    void getAllEmployeeProfiles() {
        when(cdcntProperties.getServices()).thenReturn(services);
        when(services.getUserService()).thenReturn(userService);
        when(userService.getUrl()).thenReturn("http://localhost:8080");
        when(userService.getGetAllEmployeeProfiles()).thenReturn("/list-employee-profiles");

        when(restClient.get()).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.uri("http://localhost:8080/list-employee-profiles")).thenReturn(requestHeadersUriSpec);
        when(requestHeadersUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(new ParameterizedTypeReference<List<AppUserDTO>>() {
        })).thenReturn(employeeProfilesList);
        List<AppUserDTO> result = adminService.getAllEmployeeProfiles();
        assertEquals(employeeProfilesList, result);
    }

    @Test
    void reviewJobPosting() {
        JobPostingDTO jobPostingDTO = new JobPostingDTO();
        jobPostingDTO.setId(1L);
        jobPostingDTO.setJobTitle("Software Engineer");
        jobPostingDTO.setStatus("INACTIVE");

        JobPostingDTO expectedResponse = new JobPostingDTO();
        expectedResponse.setId(1L);
        expectedResponse.setJobTitle("Software Engineer");
        expectedResponse.setStatus("ACTIVE");

        when(cdcntProperties.getServices()).thenReturn(services);
        when(services.getJobService()).thenReturn(jobService);
        when(jobService.getUrl()).thenReturn("http://localhost:8080");
        when(jobService.getReviewJobPosting()).thenReturn("/jobpostings/review");

        when(restClient.post()).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.uri("http://localhost:8080/jobpostings/review")).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.body(jobPostingDTO)).thenReturn(requestBodyUriSpec);
        when(requestBodyUriSpec.retrieve()).thenReturn(responseSpec);
        when(responseSpec.body(new ParameterizedTypeReference<JobPostingDTO>() {
        })).thenReturn(expectedResponse);

        JobPostingDTO result = adminService.reviewJobPosting(jobPostingDTO);
        assertEquals(expectedResponse, result);
        verify(requestBodyUriSpec).uri("http://localhost:8080/jobpostings/review");
        verify(requestBodyUriSpec).body(jobPostingDTO);
    }
}