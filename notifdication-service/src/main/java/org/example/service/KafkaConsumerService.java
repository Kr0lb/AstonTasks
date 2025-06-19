package org.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final EmailService emailService;

    @KafkaListener(topics = "user-create", groupId = "notification-group")
    public void listenUserCreate(String message) {
        emailService.sendEmail("pivamaks@gmail.com", "subject", message);
        System.err.println("create");
    }
}
