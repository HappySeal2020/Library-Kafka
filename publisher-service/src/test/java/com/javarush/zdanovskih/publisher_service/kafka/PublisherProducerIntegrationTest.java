package com.javarush.zdanovskih.publisher_service.kafka;
import com.javarush.zdanovskih.events.PublisherCreatedEvent;
import com.javarush.zdanovskih.publisher_service.AbstractIntegrationTest;
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

import static com.javarush.zdanovskih.constant.Topics.PUBLISHER_CREATED;
import static org.assertj.core.api.Assertions.assertThat;

@SpringBootTest
public class PublisherProducerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    ConsumerFactory<String, PublisherCreatedEvent> consumerFactory;

    @Test
    void shouldSendAuthorCreatedEvent(){

        // create test consumer
        Consumer<String, PublisherCreatedEvent> consumer =
                consumerFactory.createConsumer();

        consumer.subscribe(List.of(PUBLISHER_CREATED));

        // given
        PublisherCreatedEvent event =
                new PublisherCreatedEvent(1L, "Test Publisher", "www.test-publisher-created");

        // when
        kafkaTemplate.send(PUBLISHER_CREATED, event);

        // then
        ConsumerRecords<String, PublisherCreatedEvent> records =
                KafkaTestUtils.getRecords(consumer, Duration.ofSeconds(5));

        assertThat(records.count()).isEqualTo(1);

        PublisherCreatedEvent received =
                records.iterator().next().value();

        assertThat(received.getName()).isEqualTo("Test Publisher");
        assertThat(received.getSite()).isEqualTo("www.test-publisher-created");
    }
}
