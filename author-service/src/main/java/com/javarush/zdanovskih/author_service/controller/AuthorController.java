package com.javarush.zdanovskih.author_service.controller;

import com.javarush.zdanovskih.author_service.repository.AuthorRepository;
import com.javarush.zdanovskih.author_service.specification.AuthorSpecification;
import jakarta.validation.Valid;

import com.javarush.zdanovskih.author_service.entity.Author;
import com.javarush.zdanovskih.author_service.service.AuthorService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@Slf4j
@RestController
@RequestMapping("/authors")
@RequiredArgsConstructor
public class AuthorController {

    private final AuthorService service;
    private final AuthorRepository authorRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Author create(@Valid @RequestBody Author author) {
        return service.create(author.getName());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}")
    public Author update(@Valid @PathVariable Long id, @Valid @RequestBody Author author) {
        if (id.equals(author.getId())) {
            return service.update(author);
            //return authorRepository.save(author);
        } else {
            log.warn ("Cannot update author with id {} because it does not match {}", id, author);
            return null;
        }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Author> getAuthors(@RequestParam(required = false) String name) {
        return authorRepository.findAll(AuthorSpecification.filter(name));
    }

}