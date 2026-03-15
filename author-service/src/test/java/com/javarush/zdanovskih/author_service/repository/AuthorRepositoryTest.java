package com.javarush.zdanovskih.author_service.repository;


import com.javarush.zdanovskih.author_service.AuthorServiceApplication;
import com.javarush.zdanovskih.author_service.entity.Author;
import com.javarush.zdanovskih.author_service.specification.AuthorSpecification;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.test.context.ContextConfiguration;
import org.testcontainers.junit.jupiter.Testcontainers;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

@DataJpaTest
@Testcontainers
@ContextConfiguration(classes = AuthorServiceApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class AuthorRepositoryTest {
    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        authorRepository.save(new Author(null, "Pushkin"));
        authorRepository.save(new Author(null, "Faronov"));
        authorRepository.save(new Author(null, "Osipov"));
    }

    @Test
    void shouldFindAuthorById() {
        Author author = new Author(null, "Test Author" );
        Author savedAuthor = entityManager.persistFlushFind(author);
        Optional<Author> foundAuthor = authorRepository.findById(savedAuthor.getId());
        assertTrue(foundAuthor.isPresent());
        assertEquals("Test Author", foundAuthor.get().getName());
    }

    @Test
    void shouldFilterByName() {
        //when
        List<Author> authors = authorRepository.findAll(AuthorSpecification.filter("push"));
        //then
        assertThat(authors).hasSize(1);
        assertThat(authors.getFirst().getName()).isEqualTo("Pushkin");
    }

    @Test
    void shouldReturnAllWhenFilterIsNull() {
        //when
        List<Author> authors = authorRepository.findAll(AuthorSpecification.filter(null));
        //then

        assertThat(authors).hasSizeGreaterThanOrEqualTo(3);
    }

}
