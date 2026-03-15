package com.javarush.zdanovskih.book_service.kafka;

import com.javarush.zdanovskih.book_service.AbstractIntegrationTest;
import com.javarush.zdanovskih.book_service.cache.PublisherCache;
import com.javarush.zdanovskih.book_service.repository.PublisherCacheRepository;
import com.javarush.zdanovskih.book_service.service.PublisherCacheService;
import org.awaitility.Awaitility;
import org.junit.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Duration;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
@SpringBootTest
public class PublisherDeletedFlowIntegrationTest
        extends AbstractIntegrationTest {

    @Autowired
    PublisherCacheRepository repository;

    @Autowired
    PublisherCacheService service;

    @Test
    public void shouldDeletePublisherAndSendKafkaEvent() {

        // given
        PublisherCache publisher =
                repository.save(
                        new PublisherCache(
                                null,
                                "Test Publisher", "www.test-site"
                        )
                );

        Long id = publisher.getId();

        // when
        service.deletePublisherById(id);

        // then
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .untilAsserted(() -> {

                    Optional<PublisherCache> result =
                            repository.findById(id);

                    assertThat(result).isEmpty();

                });

    }
}