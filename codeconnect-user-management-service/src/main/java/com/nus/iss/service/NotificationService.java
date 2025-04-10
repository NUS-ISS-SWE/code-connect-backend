package com.nus.iss.service;

import com.nus.iss.entity.AppUser;

public interface NotificationService {
    void sendActivationEmail(AppUser savedAppUser);
}
