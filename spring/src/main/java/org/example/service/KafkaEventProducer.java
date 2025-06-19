package org.example.service;

import lombok.RequiredArgsConstructor;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(String email, String action) {
        kafkaTemplate.send("user-events", "%s:%s".formatted(action, email));
    }
}
