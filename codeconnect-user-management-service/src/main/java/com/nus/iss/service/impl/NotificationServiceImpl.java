package com.nus.iss.service.impl;

import com.nus.iss.entity.AppUser;
import com.nus.iss.service.NotificationService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;
import org.springframework.stereotype.Service;

import java.util.Base64;

@Service
@Slf4j
public class NotificationServiceImpl implements NotificationService {
    private final JavaMailSender javaMailSender;

    @Autowired
    public NotificationServiceImpl(JavaMailSender javaMailSender) {
        this.javaMailSender = javaMailSender;
    }

    @Override
    public void sendActivationEmail(AppUser savedAppUser) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(savedAppUser.getEmail());
        message.setSubject("Activate your CodeConnect account");
        message.setText("Please click the link to activate your account: http://www.codeconnect.com/api/v1/activate?token="
                + Base64.getEncoder().encodeToString(savedAppUser.getUsername().getBytes()));
        javaMailSender.send(message);
    }

    @Override
    public void sendDeletionEmail(AppUser appUser) {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(appUser.getEmail());
        message.setSubject("Your CodeConnect account has been deleted");
        message.setText("Your CodeConnect account has been deleted. If you did not request this, please contact support.");
        javaMailSender.send(message);
    }
}
