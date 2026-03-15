package com.javarush.zdanovskih.author_service.kafka;

import com.javarush.zdanovskih.author_service.AbstractIntegrationTest;
import com.javarush.zdanovskih.events.AuthorCreatedEvent;
import org.apache.kafka.clients.consumer.Consumer;
import org.apache.kafka.clients.consumer.ConsumerRecords;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.ConsumerFactory;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.kafka.test.utils.KafkaTestUtils;

import java.time.Duration;
import java.util.List;

import static com.javarush.zdanovskih.constant.Topics.AUTHOR_CREATED;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class AuthorProducerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    ConsumerFactory<String, AuthorCreatedEvent> consumerFactory;

    @Test
    void shouldSendAuthorCreatedEvent(){

        // create test consumer
        Consumer<String, AuthorCreatedEvent> consumer =
                consumerFactory.createConsumer();

        consumer.subscribe(List.of(AUTHOR_CREATED));

        // given
        AuthorCreatedEvent event =
                new AuthorCreatedEvent(1L, "Test Author");

        // when
        kafkaTemplate.send(AUTHOR_CREATED, event);

        // then
        ConsumerRecords<String, AuthorCreatedEvent> records =
                KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));

        assertThat(records.count()).isEqualTo(1);

        AuthorCreatedEvent received =
                records.iterator().next().value();

        assertThat(received.getName()).isEqualTo("Test Author");
    }
}
