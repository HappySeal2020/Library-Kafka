package com.javarush.zdanovskih.book_service.controller;
import com.javarush.zdanovskih.book_service.service.PublisherCacheService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.*;

import static com.javarush.zdanovskih.constant.Mapping.REST_PUBLISHER_PATH;

@Slf4j
@RestController
@RequestMapping(REST_PUBLISHER_PATH)
public class PublisherCacheController {
    private final PublisherCacheService publisherCacheService;


    public PublisherCacheController(PublisherCacheService publisherCacheService) {
        this.publisherCacheService = publisherCacheService;
    }

    //DELETE
    @DeleteMapping("/{id}")
    @ResponseStatus(HttpStatus.NO_CONTENT)
    public void deletePublisher(@PathVariable Long id) {
        log.info("Try to delete publisher in cache: {}", id);
        publisherCacheService.deletePublisherById(id);
    }
}
