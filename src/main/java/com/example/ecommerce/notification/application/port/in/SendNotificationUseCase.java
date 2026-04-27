package com.example.ecommerce.notification.application.port.in;

import com.example.ecommerce.notification.domain.model.Notification;
import com.example.ecommerce.notification.domain.model.NotificationType;

public interface SendNotificationUseCase {

    Notification sendOrderConfirmation(Long userId, String recipientEmail, String orderNumber, double totalAmount);

    Notification sendOrderShipped(Long userId, String recipientEmail, String orderNumber);

    Notification sendWelcome(Long userId, String recipientEmail, String userName);

    Notification sendPasswordReset(Long userId, String recipientEmail, String resetToken);

    Notification sendNotification(Long userId, String recipientEmail, NotificationType type, String subject, String body);
}
