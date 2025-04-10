package com.nus.iss.service.impl;

import com.nus.iss.config.CdcntProperties;
import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.service.AdminService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

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

        List<AppUserDTO> body = restClient.get()
                .uri(url)
                .retrieve()
                .body(new ParameterizedTypeReference<List<AppUserDTO>>() {
                });
        log.info("List of employer users: {}", body);
        return body;
    }
}
