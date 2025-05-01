package com.nus.iss.service.impl;

import com.nus.iss.entity.AppUser;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.Base64;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class NotificationServiceImplTest {

    @InjectMocks
    private NotificationServiceImpl notificationService;

    @Mock
    private JavaMailSender javaMailSender;

    private AppUser appUser;

    @BeforeEach
    void setUp() {
        appUser = new AppUser();
        appUser.setUsername("testuser");
        appUser.setEmail("testuser@example.com");
    }

    @Test
    void sendActivationEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(appUser.getEmail());
        message.setSubject("Activate your CodeConnect account");
        message.setText("Please click the link to activate your account: http://localhost:8080/api/v1/activate?token="
                + Base64.getEncoder().encodeToString(appUser.getUsername().getBytes()));

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        notificationService.sendActivationEmail(appUser);

        verify(javaMailSender, times(1)).send(refEq(message));
    }

    @Test
    void sendDeletionEmail() {
        SimpleMailMessage message = new SimpleMailMessage();
        message.setTo(appUser.getEmail());
        message.setSubject("Your CodeConnect account has been deleted");
        message.setText("Your CodeConnect account has been deleted. If you did not request this, please contact support.");

        doNothing().when(javaMailSender).send(any(SimpleMailMessage.class));

        notificationService.sendDeletionEmail(appUser);

        verify(javaMailSender, times(1)).send(refEq(message));
    }
}