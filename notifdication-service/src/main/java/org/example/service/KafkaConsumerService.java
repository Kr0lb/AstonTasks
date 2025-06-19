package org.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final EmailService emailService;

    @KafkaListener(topics = "user-create", groupId = "notification-group")
    public void listenUserCreate(String email) {
        emailService.sendEmail(email, "Create", "Здравствуйте! Ваш аккаунт на сайте forum был успешно создан.");
        System.err.println("create");
    }

    @KafkaListener(topics = "user-delete", groupId = "notification-group")
    public void listenUserDelete(String email) {
        emailService.sendEmail(email, "Delete", "Здравствуйте! Ваш аккаунт был удалён.");
        System.err.println("create");
    }
}
