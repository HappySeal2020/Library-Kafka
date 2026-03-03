package com.javarush.zdanovskih.publisher_service.repository;
import com.javarush.zdanovskih.publisher_service.PublisherServiceApplication;
import com.javarush.zdanovskih.publisher_service.entity.Publisher;
import com.javarush.zdanovskih.publisher_service.specification.PublisherSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@ContextConfiguration(classes = PublisherServiceApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class PublisherRepositoryTest {

    @Autowired
    private PublisherRepository publisherRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        publisherRepository.save(new Publisher(null, "first publisher", "www.first-publisher.com"));
        publisherRepository.save(new Publisher(null, "second publisher", "www.second-publisher.com"));
    }

    @Test
    void shouldFindPublisherById() {
        Publisher publisher = new Publisher(null, "Test Publisher", "www.test-publisher.com" );
        Publisher savedPublisher = entityManager.persistFlushFind(publisher);
        Optional<Publisher> foundPublisher = publisherRepository.findById(savedPublisher.getId());
        assertTrue(foundPublisher.isPresent());
        assertEquals("Test Publisher", foundPublisher.get().getName());
    }

    @Test
    void shouldFilterByName() {
        //when
        List<Publisher> publishers = publisherRepository.findAll(PublisherSpecification.filter("irs", ""));
        //then
        assertThat(publishers).hasSize(1);
        assertThat(publishers.get(0).getName()).isEqualTo("first publisher");
    }

    @Test
    void shouldReturnAllWhenFilterIsNull() {
        //when
        List<Publisher> publishers = publisherRepository.findAll(PublisherSpecification.filter(null, null));
        //then

        assertThat(publishers).hasSizeGreaterThanOrEqualTo(3);
    }

}
