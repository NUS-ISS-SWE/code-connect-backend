package com.nus.iss.service;

import com.nus.iss.dto.AppUserDTO;
import com.nus.iss.dto.JwtAccessTokenDTO;
import com.nus.iss.entity.AppUser;

public interface AppUserService {
    AppUserDTO registerUser(AppUserDTO appUserDTO);

    JwtAccessTokenDTO login(String username, String password);

    AppUser updatePassword(AppUserDTO appUserDTO);

    void activateUser(String token);
}
