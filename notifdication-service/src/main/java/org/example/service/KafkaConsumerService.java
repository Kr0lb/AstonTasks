package org.example.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Service;

@Slf4j
@Service
@RequiredArgsConstructor
public class KafkaConsumerService {
    private final EmailService emailService;

    @KafkaListener(topics = "user-events", groupId = "notification-group")
    public void listenUserCreate(String message) {
        String[] parts = message.split(":");
        String events = parts[0];
        String email = parts[1];

        switch (events) {
            case "CREATE" ->
                    emailService.sendEmail(email, "Создание аккаунта", "Здравствуйте! Ваш аккаунт на сайте forum был успешно создан.");
            case "DELETE" ->
                    emailService.sendEmail(email, "Удаление аккаунта", "Здравствуйте! Ваш аккаунт был удалён.");
        }
    }
}
