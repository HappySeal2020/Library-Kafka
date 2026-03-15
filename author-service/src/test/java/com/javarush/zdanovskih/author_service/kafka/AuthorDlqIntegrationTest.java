package com.javarush.zdanovskih.author_service.kafka;

import com.javarush.zdanovskih.author_service.AbstractIntegrationTest;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.junit.jupiter.api.Test;
import org.springframework.kafka.test.utils.KafkaTestUtils;
import org.testcontainers.shaded.org.awaitility.Awaitility;

import java.time.Duration;
import java.util.List;

import static com.javarush.zdanovskih.constant.Topics.*;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AuthorDlqIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    ConsumerFactory<String, Object> consumerFactory;

    @Test
    void shouldSendInvalidEventToDlq() {

        try (
                Consumer<String, Object> consumer =
                        consumerFactory.createConsumer()) {

            consumer.subscribe(List.of(AUTHOR_DELETED_DLT));

            consumer.poll(Duration.ofMillis(100));
            consumer.seekToEnd(consumer.assignment());
            // invalid event
            String invalidPayload = "invalid-json";

            // when
            kafkaTemplate.send(AUTHOR_DELETED, invalidPayload);

            // then
            Awaitility.await()
                    .atMost(Duration.ofSeconds(10))
                    .untilAsserted(() -> {
                        ConsumerRecords<String, Object> records =
                                KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(10));
                        assertThat(records.count()).isEqualTo(1);
                    });
        }
    }

}
