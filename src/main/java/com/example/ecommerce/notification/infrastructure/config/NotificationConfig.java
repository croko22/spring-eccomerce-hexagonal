package com.example.ecommerce.notification.infrastructure.config;

import com.example.ecommerce.notification.application.port.out.EmailSenderPort;
import com.example.ecommerce.notification.application.port.out.NotificationRepositoryPort;
import com.example.ecommerce.notification.application.service.NotificationService;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class NotificationConfig {

    @Bean
    public NotificationService notificationService(EmailSenderPort emailSenderPort,
                                                   NotificationRepositoryPort notificationRepositoryPort) {
        return new NotificationService(emailSenderPort, notificationRepositoryPort);
    }
}
