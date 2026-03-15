package com.javarush.zdanovskih.publisher_service.kafka;
import com.javarush.zdanovskih.events.PublisherDeletedEvent;
import com.javarush.zdanovskih.publisher_service.AbstractIntegrationTest;
import com.javarush.zdanovskih.publisher_service.entity.Publisher;
import com.javarush.zdanovskih.publisher_service.repository.PublisherRepository;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.util.Optional;

import static com.javarush.zdanovskih.constant.Topics.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Slf4j
@SpringBootTest
public class PublisherDeletedConsumerIntegrationTest extends AbstractIntegrationTest {

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    PublisherRepository publisherRepository;

    @BeforeEach
    void setup(){
        publisherRepository.deleteAll();
    }

    @Test
    public void shouldConsumePublisherDeletedEventAndSaveToDatabase() {
        // given
        Publisher publisher = new Publisher(null, "Test delete publisher", "www.test-delete-publisher.org");
        publisherRepository.save(publisher);
        Long publisherId = publisher.getId();

        assertThat(publisherRepository.findById(publisherId)).isPresent();
        assertThat(publisherRepository.findById(publisherId).get().getName()).isEqualTo("Test delete publisher");
        assertThat(publisherRepository.findById(publisherId).get().getSite()).isEqualTo("www.test-delete-publisher.org");


        PublisherDeletedEvent event =
                new PublisherDeletedEvent(publisherId);
        // when
        kafkaTemplate.send(
                PUBLISHER_DELETED,
                event
        );

        // then
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    Optional<Publisher> deletedPublisher =
                            publisherRepository.findById(publisherId);
                    assertThat(deletedPublisher).isNotPresent();
                });
        log.info("Test {} passed successfully", PUBLISHER_DELETED);
    }
}
