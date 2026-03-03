package com.javarush.zdanovskih.book_service.repository;

import com.javarush.zdanovskih.book_service.BookServiceApplication;
import com.javarush.zdanovskih.book_service.cache.AuthorCache;
import com.javarush.zdanovskih.book_service.cache.PublisherCache;
import com.javarush.zdanovskih.book_service.entity.Book;
import com.javarush.zdanovskih.book_service.specification.BookSpecification;
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
@ContextConfiguration(classes = BookServiceApplication.class)
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE)
public class BookRepositoryTest {

    @Autowired
    private BookRepository bookRepository;

    @Autowired
    private TestEntityManager entityManager;

    @BeforeEach
    void setUp() {
        long maxId = Long.MAX_VALUE-2;
        AuthorCache authorCache = entityManager.persist(new AuthorCache(maxId,"A prolific author"));
        PublisherCache publisherCache = entityManager.persist(new PublisherCache(maxId,"Fast print publisher","www.fast-print-publisher.com"));
        bookRepository.save(new Book(null, "First book", List.of(authorCache), 2000, publisherCache, "bbk1", "isbn1", 1000));
        bookRepository.save(new Book(null, "Second book", List.of(authorCache), 2005, publisherCache, "bbk2", "isbn2", 800));
        bookRepository.save(new Book(null, "Third book", List.of(authorCache), 2010, publisherCache, "bbk3", "isbn3", 900));
    }

    @Test
    void shouldFindBookById() {
        long maxLong=Long.MAX_VALUE;
        AuthorCache authorCache = entityManager.persist(new AuthorCache(maxLong,"Писатель"));
        PublisherCache publisherCache = entityManager.persist(new PublisherCache(maxLong,"Издатель","www.Издатель.ru"));
        entityManager.persist(authorCache);
        entityManager.persist(publisherCache);
        Book book = entityManager.persist(new Book(null, "Test book", List.of(authorCache), 2000, publisherCache, "bbk1", "isbn1", 1000));
        entityManager.flush();
        Optional<Book> foundBook = bookRepository.findById(book.getId());
        assertTrue(foundBook.isPresent());
        assertEquals("Test book", foundBook.get().getName());
    }


    @Test
    void shouldFilterByName() {
        //when
        List<Book> books = bookRepository.findAll(BookSpecification.filter("first ", null, null,
                null, null, null, null, null, null));
        //then
        assertThat(books).hasSize(1);
        assertThat(books.get(0).getName()).isEqualTo("First book");
    }

    @Test
    void shouldReturnAllWhenFilterIsNull() {
        //when
        List<Book> books = bookRepository.findAll(BookSpecification.filter(null, null, null,
                null, null, null, null, null, null));
        //then

        assertThat(books).hasSizeGreaterThanOrEqualTo(3);
    }

}
