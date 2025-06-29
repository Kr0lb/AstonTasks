package org.example.service;

import lombok.RequiredArgsConstructor;
import org.example.enums.MsgType;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class KafkaEventProducer {
    private final KafkaTemplate<String, String> kafkaTemplate;

    public void send(MsgType action, String email) {
        kafkaTemplate.send("user-events", "%s:%s".formatted(action, email));
    }
}
