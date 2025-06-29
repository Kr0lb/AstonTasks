package org.example.service;

import org.example.enums.MsgType;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.kafka.core.KafkaTemplate;

import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class KafkaEventProducerTest {

    @Mock
    private KafkaTemplate<String, String> kafkaTemplate;

    private KafkaEventProducer kafkaEventProducer;

    @BeforeEach
    void setUp() {
        kafkaEventProducer = new KafkaEventProducer(kafkaTemplate);
    }

    @Test
    void send() {
        kafkaEventProducer.send(MsgType.CREATE,"test@example.com");

        verify(kafkaTemplate, times(1)).send(
                eq("user-events"),
                eq("CREATE:test@example.com")
        );
    }
}