package com.javarush.zdanovskih.book_service.service;
import com.javarush.zdanovskih.book_service.cache.AuthorCache;
import com.javarush.zdanovskih.book_service.cache.PublisherCache;
import com.javarush.zdanovskih.book_service.entity.Book;
import com.javarush.zdanovskih.book_service.exception.NotFoundException;
import com.javarush.zdanovskih.book_service.repository.AuthorCacheRepository;
import com.javarush.zdanovskih.book_service.repository.BookRepository;
import com.javarush.zdanovskih.book_service.repository.PublisherCacheRepository;
import com.javarush.zdanovskih.book_service.specification.BookSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.ArrayList;
import java.util.List;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class BookService {
    private final BookRepository bookRepository;
    private final PublisherCacheRepository publisherCacheRepository;
    private final AuthorCacheRepository authorCacheRepository;

    //CREATE
    @Transactional
    public Book create(Book book) {

        List<AuthorCache> authors = new ArrayList<>(book.getAuthors());
        for (AuthorCache authorCache : authors) {
            authorCache.setName(authorCacheRepository.findById(authorCache.getId()).get().getName());
        }
        book.setAuthors(authors);
        PublisherCache publisher = new PublisherCache();
        publisher.setId(book.getPublisher().getId());
        publisher.setName(publisherCacheRepository.findById(book.getPublisher().getId()).get().getName());
        book.setPublisher(publisher);

        Book saved = bookRepository.save(book);
        log.info("Creating or updating book: {}", book);
        return saved;
    }

    //READ
    public Book getById(Long id) {
        return bookRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Book not found id=" + id));
    }


    public List<Book> getAllBooks(String name,
                                  String author,
                                  Integer printYearfrom,
                                  Integer printYearto,
                                  String publisher,
                                  String bbk,
                                  String isbn,
                                  Integer pagesFrom,
                                  Integer pagesTo) {
        Sort sort = Sort.sort(Book.class).by(Book::getId);
        return bookRepository.findAll(BookSpecification.filter(name, author, printYearfrom, printYearto, publisher, bbk, isbn, pagesFrom, pagesTo),sort);
    }


    //DELETE
    @Transactional
    public void deleteBookById(Long id) {
        Book book = getById(id);
        bookRepository.delete(book);
        log.info("Book deleted id={}", id);
    }

}