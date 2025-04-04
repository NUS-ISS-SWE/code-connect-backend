package com.nus.iss.service;

import com.nus.iss.dto.AppUserDto;
import com.nus.iss.dto.JwtAccessTokenDTO;
import com.nus.iss.entity.AppUser;

public interface AppUserService {
    AppUser registerUser(AppUser appUser);
    JwtAccessTokenDTO login(String username, String password);
    AppUser updatePassword(AppUserDto appUserDto);

    void activateUser(String token);
}
