package org.example.service;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.TestInstance;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.context.EmbeddedKafka;
import org.springframework.mail.SimpleMailMessage;
import org.springframework.mail.javamail.JavaMailSender;

import java.util.concurrent.CountDownLatch;
import java.util.concurrent.TimeUnit;

import static org.junit.jupiter.api.Assertions.assertTrue;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.doAnswer;

@SpringBootTest
@EmbeddedKafka(partitions = 1, topics = "user-events")
@TestInstance(TestInstance.Lifecycle.PER_CLASS)
class KafkaConsumerServiceTest {

    @Autowired
    private KafkaTemplate<String, String> kafkaTemplate;

    @MockBean
    private JavaMailSender mailSender;

    @Test
    public void testKafkaEmailSendingCREATE() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(mailSender).send(any(SimpleMailMessage.class));

        kafkaTemplate.send("user-events", "CREATE:test@example.com");

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }

    @Test
    public void testKafkaEmailSendingDELETE() throws InterruptedException {
        CountDownLatch latch = new CountDownLatch(1);

        doAnswer(invocation -> {
            latch.countDown();
            return null;
        }).when(mailSender).send(any(SimpleMailMessage.class));

        kafkaTemplate.send("user-events", "DELETE:test@example.com");

        assertTrue(latch.await(5, TimeUnit.SECONDS));
    }
}