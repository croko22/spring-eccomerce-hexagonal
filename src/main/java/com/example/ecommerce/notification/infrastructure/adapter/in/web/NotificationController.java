package com.example.ecommerce.notification.infrastructure.adapter.in.web;

import com.example.ecommerce.notification.application.port.in.SendNotificationUseCase;
import com.example.ecommerce.notification.domain.model.Notification;
import com.example.ecommerce.notification.infrastructure.adapter.in.web.dto.SendNotificationRequest;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/notifications")
public class NotificationController {

    private final SendNotificationUseCase sendNotificationUseCase;

    public NotificationController(SendNotificationUseCase sendNotificationUseCase) {
        this.sendNotificationUseCase = sendNotificationUseCase;
    }

    @PostMapping("/send")
    public ResponseEntity<Notification> sendNotification(@RequestBody SendNotificationRequest request) {
        Notification notification = sendNotificationUseCase.sendNotification(
                request.getUserId(),
                request.getRecipientEmail(),
                request.getType(),
                request.getSubject(),
                request.getBody()
        );
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/order-confirmation")
    public ResponseEntity<Notification> sendOrderConfirmation(
            @RequestParam Long userId,
            @RequestParam String email,
            @RequestParam String orderNumber,
            @RequestParam double totalAmount) {
        Notification notification = sendNotificationUseCase.sendOrderConfirmation(
                userId, email, orderNumber, totalAmount
        );
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/order-shipped")
    public ResponseEntity<Notification> sendOrderShipped(
            @RequestParam Long userId,
            @RequestParam String email,
            @RequestParam String orderNumber) {
        Notification notification = sendNotificationUseCase.sendOrderShipped(
                userId, email, orderNumber
        );
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/welcome")
    public ResponseEntity<Notification> sendWelcome(
            @RequestParam Long userId,
            @RequestParam String email,
            @RequestParam String userName) {
        Notification notification = sendNotificationUseCase.sendWelcome(
                userId, email, userName
        );
        return ResponseEntity.ok(notification);
    }

    @PostMapping("/password-reset")
    public ResponseEntity<Notification> sendPasswordReset(
            @RequestParam Long userId,
            @RequestParam String email,
            @RequestParam String resetToken) {
        Notification notification = sendNotificationUseCase.sendPasswordReset(
                userId, email, resetToken
        );
        return ResponseEntity.ok(notification);
    }
}
