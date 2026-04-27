package com.example.ecommerce.notification.domain.model;

import java.time.LocalDateTime;

public class Notification {

    private Long id;
    private Long userId;
    private String recipientEmail;
    private NotificationType type;
    private String subject;
    private String body;
    private boolean sent;
    private LocalDateTime createdAt;
    private LocalDateTime sentAt;

    public Notification(Long id, Long userId, String recipientEmail, NotificationType type,
                        String subject, String body, boolean sent,
                        LocalDateTime createdAt, LocalDateTime sentAt) {
        if (recipientEmail == null || recipientEmail.trim().isEmpty() || !recipientEmail.contains("@")) {
            throw new IllegalArgumentException("Invalid recipient email");
        }
        if (type == null) {
            throw new IllegalArgumentException("Notification type cannot be null");
        }
        if (subject == null || subject.trim().isEmpty()) {
            throw new IllegalArgumentException("Subject cannot be empty");
        }
        if (body == null || body.trim().isEmpty()) {
            throw new IllegalArgumentException("Body cannot be empty");
        }

        this.id = id;
        this.userId = userId;
        this.recipientEmail = recipientEmail;
        this.type = type;
        this.subject = subject;
        this.body = body;
        this.sent = sent;
        this.createdAt = createdAt != null ? createdAt : LocalDateTime.now();
        this.sentAt = sentAt;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Long getUserId() {
        return userId;
    }

    public void setUserId(Long userId) {
        this.userId = userId;
    }

    public String getRecipientEmail() {
        return recipientEmail;
    }

    public void setRecipientEmail(String recipientEmail) {
        this.recipientEmail = recipientEmail;
    }

    public NotificationType getType() {
        return type;
    }

    public void setType(NotificationType type) {
        this.type = type;
    }

    public String getSubject() {
        return subject;
    }

    public void setSubject(String subject) {
        this.subject = subject;
    }

    public String getBody() {
        return body;
    }

    public void setBody(String body) {
        this.body = body;
    }

    public boolean isSent() {
        return sent;
    }

    public void setSent(boolean sent) {
        this.sent = sent;
    }

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getSentAt() {
        return sentAt;
    }

    public void setSentAt(LocalDateTime sentAt) {
        this.sentAt = sentAt;
    }

    public void markAsSent() {
        this.sent = true;
        this.sentAt = LocalDateTime.now();
    }
}
