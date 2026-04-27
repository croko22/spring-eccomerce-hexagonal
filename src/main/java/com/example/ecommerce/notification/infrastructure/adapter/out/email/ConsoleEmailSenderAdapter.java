package com.example.ecommerce.notification.infrastructure.adapter.out.email;

import com.example.ecommerce.notification.application.port.out.EmailSenderPort;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
public class ConsoleEmailSenderAdapter implements EmailSenderPort {

    private static final Logger logger = LoggerFactory.getLogger(ConsoleEmailSenderAdapter.class);

    @Override
    public void sendEmail(String to, String subject, String body) {
        logger.info("========================================");
        logger.info("EMAIL SENT (Console Mock)");
        logger.info("To: {}", to);
        logger.info("Subject: {}", subject);
        logger.info("----------------------------------------");
        logger.info("Body: {}", body);
        logger.info("========================================");
    }
}
