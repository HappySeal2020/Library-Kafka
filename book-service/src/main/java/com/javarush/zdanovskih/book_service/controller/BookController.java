package com.javarush.zdanovskih.book_service.controller;

import com.javarush.zdanovskih.book_service.entity.Book;
import com.javarush.zdanovskih.book_service.service.BookService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static com.javarush.zdanovskih.constant.Mapping.REST_BOOK_PATH;

@Slf4j
@RestController
@RequestMapping(REST_BOOK_PATH)
//@RequiredArgsConstructor
public class BookController {

    private final BookService bookService;

    public BookController(BookService bookService) {
        this.bookService = bookService;
    }

    //CREATE
    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Book createBook(@RequestBody Book book) {
        book.setId(null);
        return bookService.create(book);
    }

    //READ
    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Book> getAllBooks(@RequestParam(required = false) String name,
                                  @RequestParam(required = false) String author,
                                  @RequestParam(required = false) Integer printYearFrom,
                                  @RequestParam(required = false) Integer printYearTo,
                                  @RequestParam(required = false) String publisher,
                                  @RequestParam(required = false) String bbk,
                                  @RequestParam(required = false) String isbn,
                                  @RequestParam(required = false) Integer pagesFrom,
                                  @RequestParam(required = false) Integer pagesTo) {
        return bookService.getAllBooks(name, author, printYearFrom,printYearTo, publisher, bbk, isbn, pagesFrom, pagesTo);
    }

    //UPDATE
    @PutMapping("/{id}")
    @ResponseStatus(HttpStatus.ACCEPTED)
    public Book updateBook(@PathVariable Long id, @RequestBody Book book) {
        if (book.getId().equals(id)) {
            log.info("Update book id: {}", id);
            return bookService.create(book);
        } else {
            log.warn("Update book SKIPPED: id {} not match to {}", id, book.getId());
            return null;
        }
    }

    //DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        log.info("Delete book: {}", id);
        bookService.deleteBookById(id);
    }
}