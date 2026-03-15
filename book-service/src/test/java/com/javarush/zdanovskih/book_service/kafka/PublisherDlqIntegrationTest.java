package com.javarush.zdanovskih.book_service.kafka;

import com.javarush.zdanovskih.book_service.AbstractIntegrationTest;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecord;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.test.annotation.DirtiesContext;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.List;
import java.util.UUID;

import static com.javarush.zdanovskih.constant.Topics.*;
import static org.assertj.core.api.Assertions.assertThat;

@DirtiesContext(classMode=DirtiesContext.ClassMode.AFTER_CLASS)
@SpringBootTest
public class PublisherDlqIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    ConsumerFactory<String, Object> consumerFactory;

    @Test
    void shouldSendInvalidCreateEventToDlq() {

        try (
        Consumer<String, Object> consumer =createTestConsumer(PUBLISHER_CREATED_DLT)) {

            // invalid event
            String invalidPayload = "invalid-json";

            // when
            kafkaTemplate.send(PUBLISHER_CREATED, invalidPayload);

            // then
            Awaitility.await()
                    .atMost(Duration.ofSeconds(10))
                    .untilAsserted(() -> {
                        ConsumerRecords<String, Object> records =
                                consumer.poll(Duration.ofMillis(500));
                        assertThat(records.count()).isGreaterThan(0);
                        ConsumerRecord<String,Object> record = records.iterator().next();
                        assertThat(record.topic()).isEqualTo(PUBLISHER_CREATED_DLT);
                    });
        }
    }

    @Test
    void shouldSendInvalidUpdateEventToDlq() {

        try (
        Consumer<String, Object> consumer = createTestConsumer(PUBLISHER_UPDATED_DLT)) {

            // invalid event
            String invalidPayload = "invalid-json";

            // when
            kafkaTemplate.send(PUBLISHER_UPDATED, invalidPayload);

            // then
            Awaitility.await()
                    .atMost(Duration.ofSeconds(10))
                    .untilAsserted(() -> {
                        ConsumerRecords<String, Object> records =
                                consumer.poll(Duration.ofMillis(500));
                        assertThat(records.count()).isGreaterThan(0);
                        ConsumerRecord<String,Object> record = records.iterator().next();
                        assertThat(record.topic()).isEqualTo(PUBLISHER_UPDATED_DLT);
                    });
        }
    }

    private Consumer <String, Object> createTestConsumer (String topic) {
        Consumer <String, Object> consumer = consumerFactory.createConsumer("test-group-" + UUID.randomUUID(),
                "client-" + UUID.randomUUID());
        consumer.subscribe(List.of(topic));
        consumer.poll(Duration.ofMillis(100));
        consumer.seekToEnd(consumer.assignment());
        return consumer;
    }

}
