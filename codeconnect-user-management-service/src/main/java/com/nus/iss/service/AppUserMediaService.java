package com.nus.iss.service;

import com.nus.iss.entity.AppUserMedia;
import org.springframework.web.multipart.MultipartFile;

public interface AppUserMediaService {

    AppUserMedia getProfilePicture(String username);

    String createProfilePicture(String username, MultipartFile file);

    void deleteProfilePicture(String username);
}
