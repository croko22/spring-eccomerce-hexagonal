package com.example.ecommerce.notification.application.port.out;

import com.example.ecommerce.notification.domain.model.Notification;

import java.util.List;
import java.util.Optional;

public interface NotificationRepositoryPort {

    Notification save(Notification notification);

    Optional<Notification> findById(Long id);

    List<Notification> findByUserId(Long userId);

    List<Notification> findPendingNotifications();
}
