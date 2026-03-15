package com.javarush.zdanovskih.book_service.kafka;

import com.javarush.zdanovskih.events.PublisherDeletedEvent;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.DynamicPropertyRegistry;
import org.springframework.test.context.DynamicPropertySource;
import org.testcontainers.junit.jupiter.Container;
import org.testcontainers.junit.jupiter.Testcontainers;
import org.testcontainers.kafka.KafkaContainer;

import static com.javarush.zdanovskih.constant.Topics.PUBLISHER_DELETED;

@Testcontainers
@SpringBootTest
public class PublisherKafkaIntegrationTest {

    @Container
    static KafkaContainer kafka =
            new KafkaContainer("apache/kafka:4.0.0");

    @DynamicPropertySource
    static void configure(DynamicPropertyRegistry registry) {

        registry.add(
                "spring.kafka.bootstrap-servers",
                kafka::getBootstrapServers
        );
    }

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    PublisherCreatedConsumer consumer;

    @Test
    public void shouldSendEvent() throws Exception {
        kafkaTemplate.send(PUBLISHER_DELETED, new PublisherDeletedEvent());
        Thread.sleep(2000);
        // проверить ещё...
    }

}
