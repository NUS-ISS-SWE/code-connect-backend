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
        message.setText("Please click the link to activate your account: http://localhost:8080/api/v1/activate?token="
                + Base64.getEncoder().encodeToString(savedAppUser.getUsername().getBytes()));
        javaMailSender.send(message);
    }

}
