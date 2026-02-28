package com.javarush.zdanovskih.book_service.kafka;
import com.javarush.zdanovskih.book_service.repository.AuthorCacheRepository;
import com.javarush.zdanovskih.events.AuthorCreatedEvent;
import com.javarush.zdanovskih.book_service.cache.AuthorCache;
import com.javarush.zdanovskih.events.AuthorUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.javarush.zdanovskih.constant.Topics.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorCreatedConsumer {

    private final AuthorCacheRepository repository;

    @KafkaListener(topics = AUTHOR_CREATED)
    @Transactional
    public void handle(AuthorCreatedEvent event, @Header (KafkaHeaders.OFFSET) Long offset) {
        if(!repository.existsById(event.getId())) {
            repository.save(
                    new AuthorCache(event.getId(), event.getName()));
            log.info("Save received AuthorCreatedEvent: {} at offset {}", event, offset);
        } else {
            log.info("Received AuthorCreatedEvent: {} - id already exists", event);
        }
    }


    @KafkaListener(topics = AUTHOR_UPDATED)
    @Transactional
    public void handle(AuthorUpdatedEvent event, @Header (KafkaHeaders.OFFSET) Long offset) {
        if(repository.existsById(event.getId())) {
            log.info("Update received AuthorCreatedEvent: {} at offset {}", event, offset);
        } else {
            log.info("Received AuthorCreatedEvent: {} - id NOT exists", event);
        }
        repository.save(
                new AuthorCache(event.getId(), event.getName()));
    }

}