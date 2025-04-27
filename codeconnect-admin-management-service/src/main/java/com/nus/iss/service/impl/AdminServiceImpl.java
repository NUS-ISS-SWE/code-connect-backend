package com.nus.iss.service.impl;

import com.nus.iss.config.CdcntProperties;
import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.dto.JobPostingDTO;
import com.nus.iss.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;
import org.springframework.web.util.UriComponentsBuilder;

import java.net.URI;
import java.util.List;

@Service
@Slf4j
public class AdminServiceImpl implements AdminService {
    private final RestClient restClient;
    private final CdcntProperties cdcntProperties;

    @Autowired
    public AdminServiceImpl(RestClient restClient, CdcntProperties cdcntProperties) {
        this.restClient = restClient;
        this.cdcntProperties = cdcntProperties;
    }

    @Override
    public List<AppUserDTO> getAllEmployerProfiles() {
        log.info("Fetching list of employer users from user service");
        String url = cdcntProperties.getServices().getUserService().getUrl()
                .concat(cdcntProperties.getServices().getUserService().getGetAllEmployerProfiles());

        List<AppUserDTO> response = restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<List<AppUserDTO>>() {
                });
        log.info("List of employer users: {}", response);
        return response;
    }

    @Override
    public AppUserDTO reviewEmployerProfile(AppUserDTO appUserDTO) {
        log.info("Reviewing employer profile: {}", appUserDTO);
        String url = cdcntProperties.getServices().getUserService().getUrl()
                .concat(cdcntProperties.getServices().getUserService().getReviewEmployerProfiles());

        AppUserDTO response = restClient.post()
                .uri(url)
                .body(appUserDTO)
                .retrieve()
                .body(new ParameterizedTypeReference<AppUserDTO>() {
                });
        log.info("Reviewed employer profile: {}", response);
        return response;
    }

    @Override
    public void deleteProfile(String username) {
        log.info("Deleting employer profile: {}", username);
        String url = cdcntProperties.getServices().getUserService().getUrl()
                .concat(cdcntProperties.getServices().getUserService().getDeleteEmployerProfiles());

        URI uri = UriComponentsBuilder
                .fromUri(URI.create(url))
                .queryParam("username", username)
                .build()
                .toUri();

        restClient.delete()
                .uri(uri)
                .retrieve()
                .body(new ParameterizedTypeReference<Void>() {
                });
    }

    @Override
    public List<AppUserDTO> getAllEmployeeProfiles() {
        log.info("Fetching list of employee users from user service");
        String url = cdcntProperties.getServices().getUserService().getUrl()
                .concat(cdcntProperties.getServices().getUserService().getGetAllEmployeeProfiles());
        List<AppUserDTO> response = restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<List<AppUserDTO>>() {
                });
        log.info("List of employee users: {}", response);
        return response;
    }

    @Override
    public JobPostingDTO reviewJobPosting(JobPostingDTO jobPostingDTO) {
        log.info("Reviewing job posting: {}", jobPostingDTO);
        String url = cdcntProperties.getServices().getJobService().getUrl()
                .concat(cdcntProperties.getServices().getJobService().getReviewJobPosting());
        JobPostingDTO response = restClient.post()
                .uri(url)
                .body(jobPostingDTO)
                .retrieve()
                .body(new ParameterizedTypeReference<JobPostingDTO>() {
                });
        log.info("Reviewed job posting: {}", response);
        return response;
    }
}
