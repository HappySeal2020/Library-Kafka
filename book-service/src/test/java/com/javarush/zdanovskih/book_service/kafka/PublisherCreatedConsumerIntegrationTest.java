package com.javarush.zdanovskih.book_service.kafka;

import com.javarush.zdanovskih.book_service.AbstractIntegrationTest;
import com.javarush.zdanovskih.book_service.cache.PublisherCache;
import com.javarush.zdanovskih.book_service.repository.PublisherCacheRepository;
import com.javarush.zdanovskih.events.PublisherCreatedEvent;
import com.javarush.zdanovskih.events.PublisherUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import org.springframework.test.context.jdbc.Sql;

import static com.javarush.zdanovskih.constant.Topics.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Slf4j
@SpringBootTest
public class PublisherCreatedConsumerIntegrationTest
        extends AbstractIntegrationTest {

    private final String testMark="Test_Publisher";

    private final Long publisherId=new Random().nextLong(Long.MAX_VALUE-100L, Long.MAX_VALUE);

    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    PublisherCacheRepository publisherCacheRepository;

    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            statements = "delete from publisher_cache where  name like '%"+testMark+"%'")
    @Test
    public void shouldConsumePublisherCreatedEventAndSaveToDatabase() {
        // given
        String name=String.format ("Created %s  %s", testMark, UUID.randomUUID());
        PublisherCreatedEvent event =
                new PublisherCreatedEvent(
                        publisherId,
                        name,
                        "www.test-site.org"
                );
        // when
        kafkaTemplate.send(
                PUBLISHER_CREATED,
                event
        );

        // then
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    Optional<PublisherCache> publisherCache =
                            publisherCacheRepository.findById(publisherId);
                    assertThat(publisherCache).isPresent();
                    assertThat(publisherCache.get().getName())
                            .isEqualTo(name);
                    assertThat(publisherCache.get().getSite())
                            .isEqualTo("www.test-site.org");
                });

    }

    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            statements = "delete from publisher_cache where  name like '%"+testMark+"%'")

    @Test
    public void shouldConsumePublisherUpdatedEventAndSaveToDatabase() {
        //prepare Publisher
        if (!publisherCacheRepository.existsById(publisherId)) {
            publisherCacheRepository.save(
                    new PublisherCache(publisherId, String.format("Existing %s %s",testMark, UUID.randomUUID()), "www.existing-test-site"));
        }

        // given
        String name=String.format ("Updated %s  %s", testMark, UUID.randomUUID());
        PublisherUpdatedEvent event =
                new PublisherUpdatedEvent(publisherId, name, "www.upd-publisher.org");
        // when
        kafkaTemplate.send(
                PUBLISHER_UPDATED,
                event
        );

        // then
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                            Optional<PublisherCache> publisherCache =
                                    publisherCacheRepository.findById(publisherId);
                    assertThat(publisherCache).isPresent();
                    assertThat(publisherCache.get().getName())
                            .isEqualTo(name);
                    assertThat(publisherCache.get().getSite())
                            .isEqualTo("www.upd-publisher.org");
                        });
    }

}
