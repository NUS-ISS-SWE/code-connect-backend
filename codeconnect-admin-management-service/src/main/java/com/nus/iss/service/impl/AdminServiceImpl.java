package com.nus.iss.service.impl;

import com.nus.iss.service.AdminService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

@Service
public class AdminServiceImpl implements AdminService {
    private final RestClient restClient;

    @Autowired
    public AdminServiceImpl(RestClient restClient) {
        this.restClient = restClient;
    }


}
