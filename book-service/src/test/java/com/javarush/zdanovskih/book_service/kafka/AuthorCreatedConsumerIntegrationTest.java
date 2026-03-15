package com.javarush.zdanovskih.book_service.kafka;

import com.javarush.zdanovskih.book_service.AbstractIntegrationTest;
import com.javarush.zdanovskih.book_service.cache.AuthorCache;
import com.javarush.zdanovskih.book_service.repository.AuthorCacheRepository;
import com.javarush.zdanovskih.events.AuthorCreatedEvent;
import com.javarush.zdanovskih.events.AuthorUpdatedEvent;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.test.context.jdbc.Sql;

import java.time.Duration;
import java.util.Optional;
import java.util.Random;
import java.util.UUID;

import static com.javarush.zdanovskih.constant.Topics.*;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Slf4j
@SpringBootTest
public class AuthorCreatedConsumerIntegrationTest
        extends AbstractIntegrationTest {

    private final String testMark="Test_Author";

    private final Long authorId=new Random().nextLong(Long.MAX_VALUE-100L, Long.MAX_VALUE);
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    AuthorCacheRepository authorCacheRepository;

    @Test
    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            statements = "delete from author_cache where  name like '%"+testMark+"%'")

    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            statements = "delete from author_cache where  name like '%"+testMark+"%'")

    public void shouldConsumeAuthorCreatedEventAndSaveToDatabase() {
        // given
        String name=String.format ("Created %s  %s", testMark, UUID.randomUUID());
        AuthorCreatedEvent event =
                new AuthorCreatedEvent(
                        authorId,
                        name
                );
        // when
        kafkaTemplate.send(
                AUTHOR_CREATED,
                event
        );

        // then
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    Optional<AuthorCache> author =
                            authorCacheRepository.findById(authorId);
                    assertThat(author).isPresent();
                    assertThat(author.get().getName())
                            .isEqualTo(name);
                });
    }

    @Test
    @Sql(
            executionPhase = Sql.ExecutionPhase.AFTER_TEST_METHOD,
            statements = "delete from author_cache where  name like '%"+testMark+"%'")

    @Sql(
            executionPhase = Sql.ExecutionPhase.BEFORE_TEST_METHOD,
            statements = "delete from author_cache where  name like '%"+testMark+"%'")

    public void shouldConsumeAuthorUpdatedEventAndSaveToDatabase() {
        //prepare Author
        if (!authorCacheRepository.existsById(authorId)) {
            authorCacheRepository.save(
                    new AuthorCache(authorId, String.format("Existing %s %s",testMark, UUID.randomUUID())));
        }
        // given
        String name=String.format("Updated %s %s ", testMark, UUID.randomUUID());
        AuthorUpdatedEvent event =
                new AuthorUpdatedEvent(authorId, name);
        // when
        kafkaTemplate.send(
                AUTHOR_UPDATED,
                event
        );

        // then
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    Optional<AuthorCache> author =
                            authorCacheRepository.findById(authorId);
                    assertThat(author).isPresent();
                    assertThat(author.get().getName())
                            .isEqualTo(name);
                });
    }
}
