package com.javarush.zdanovskih.book_service.controller;
import com.javarush.zdanovskih.book_service.service.AuthorCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.javarush.zdanovskih.constant.Mapping.REST_AUTHOR_PATH;

@Slf4j
@RestController
@RequestMapping(REST_AUTHOR_PATH)
public class AuthorCacheController {

    private final AuthorCacheService authorCacheService;


    public AuthorCacheController(AuthorCacheService authorCacheService) {
        this.authorCacheService = authorCacheService;
    }

    //DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deleteBook(@PathVariable Long id) {
        log.info("Try to delete author in cache: {}", id);
        authorCacheService.deleteAuthorById(id);
    }
}
