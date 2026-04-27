package com.example.ecommerce.notification.infrastructure.adapter.out.persistence;

import com.example.ecommerce.notification.application.port.out.NotificationRepositoryPort;
import com.example.ecommerce.notification.domain.model.Notification;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;

@Component
public class NotificationPersistenceAdapter implements NotificationRepositoryPort {

    private final NotificationJpaRepository notificationJpaRepository;

    public NotificationPersistenceAdapter(NotificationJpaRepository notificationJpaRepository) {
        this.notificationJpaRepository = notificationJpaRepository;
    }

    @Override
    public Notification save(Notification notification) {
        NotificationEntity entity = NotificationEntity.fromDomain(notification);
        NotificationEntity saved = notificationJpaRepository.save(entity);
        return saved.toDomain();
    }

    @Override
    public Optional<Notification> findById(Long id) {
        return notificationJpaRepository.findById(id)
                .map(NotificationEntity::toDomain);
    }

    @Override
    public List<Notification> findByUserId(Long userId) {
        return notificationJpaRepository.findByUserId(userId)
                .stream()
                .map(NotificationEntity::toDomain)
                .toList();
    }

    @Override
    public List<Notification> findPendingNotifications() {
        return notificationJpaRepository.findBySent(false)
                .stream()
                .map(NotificationEntity::toDomain)
                .toList();
    }
}
