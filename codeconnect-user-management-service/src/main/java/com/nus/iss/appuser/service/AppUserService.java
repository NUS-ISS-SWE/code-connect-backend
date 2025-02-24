package com.nus.iss.appuser.service;

import com.nus.iss.appuser.dto.JwtAccessTokenDTO;
import com.nus.iss.appuser.entity.AppUser;

public interface AppUserService {
    AppUser registerUser(AppUser appUser);
    JwtAccessTokenDTO login(String username, String password);
}
