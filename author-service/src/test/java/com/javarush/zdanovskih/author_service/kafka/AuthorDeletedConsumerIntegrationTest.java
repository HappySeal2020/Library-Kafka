package com.javarush.zdanovskih.author_service.kafka;

import com.javarush.zdanovskih.author_service.AbstractIntegrationTest;
import com.javarush.zdanovskih.author_service.entity.Author;
import com.javarush.zdanovskih.author_service.repository.AuthorRepository;
import com.javarush.zdanovskih.events.AuthorDeletedEvent;
import lombok.extern.slf4j.Slf4j;
import org.awaitility.Awaitility;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.kafka.core.KafkaTemplate;

import java.time.Duration;
import java.util.Optional;

import static com.javarush.zdanovskih.constant.Topics.AUTHOR_DELETED;
import static org.assertj.core.api.AssertionsForInterfaceTypes.assertThat;

@Slf4j
@SpringBootTest
public class AuthorDeletedConsumerIntegrationTest extends AbstractIntegrationTest {
    @Autowired
    KafkaTemplate<String, Object> kafkaTemplate;

    @Autowired
    AuthorRepository authorRepository;


    @BeforeEach
    void setup(){
        authorRepository.deleteAll();
    }

    @Test
    public void shouldConsumeAuthorDeletedEventAndSaveToDatabase() {
        // given
        Author author = new Author();
        author.setName("Delete Test Author");
        authorRepository.save(author);
        Long authorId = author.getId();

        assertThat(authorRepository.findById(authorId)).isPresent();
        assertThat(authorRepository.findById(authorId).get().getName()).isEqualTo("Delete Test Author");

        AuthorDeletedEvent event =
                new AuthorDeletedEvent(authorId);
        // when
        kafkaTemplate.send(
                AUTHOR_DELETED,
                event
        );

        // then
        Awaitility.await()
                .atMost(Duration.ofSeconds(10))
                .pollInterval(Duration.ofMillis(500))
                .untilAsserted(() -> {
                    Optional<Author> deletedAuthor =
                            authorRepository.findById(authorId);
                    assertThat(deletedAuthor).isNotPresent();
                });
        log.info("Test {} passed successfully", AUTHOR_DELETED);
    }
}
