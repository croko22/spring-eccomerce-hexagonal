package com.example.ecommerce.notification.application.port.out;

import com.example.ecommerce.notification.domain.model.Notification;

public interface EmailSenderPort {

    void sendEmail(String to, String subject, String body);
}
