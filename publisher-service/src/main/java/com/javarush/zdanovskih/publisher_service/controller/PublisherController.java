package com.javarush.zdanovskih.publisher_service.controller;


import com.javarush.zdanovskih.publisher_service.entity.Publisher;
import com.javarush.zdanovskih.publisher_service.repository.PublisherRepository;
import com.javarush.zdanovskih.publisher_service.service.PublisherService;
import com.javarush.zdanovskih.publisher_service.specification.PublisherSpecification;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;
import jakarta.validation.Valid;

import java.util.List;

@Slf4j
@RestController
@RequestMapping("/publishers")
@RequiredArgsConstructor
public class PublisherController {

    private final PublisherService service;
    private final PublisherRepository publisherRepository;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public Publisher create(@RequestBody Publisher publisher) {
        return service.create(publisher.getName(), publisher.getSite());
    }

    @ResponseStatus(HttpStatus.ACCEPTED)
    @PutMapping("/{id}")
    public void update(@PathVariable Long id, @Valid @RequestBody Publisher publisher) {
            if (id.equals(publisher.getId())) {
                service.update(publisher);
            } else {
                log.warn ("Cannot update publisher with id {} because it does not match {}", id, publisher);
            }
    }

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public List<Publisher> getPublishers(@RequestParam(required = false) String name,
                                         @RequestParam(required = false) String site) {
        return publisherRepository.findAll(PublisherSpecification.filter(name, site));
    }

}
