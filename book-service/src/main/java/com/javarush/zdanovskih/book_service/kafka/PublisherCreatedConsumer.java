package com.javarush.zdanovskih.book_service.kafka;

import com.javarush.zdanovskih.book_service.cache.PublisherCache;
import com.javarush.zdanovskih.book_service.repository.PublisherCacheRepository;
import com.javarush.zdanovskih.events.PublisherCreatedEvent;
import com.javarush.zdanovskih.events.PublisherUpdatedEvent;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.kafka.support.KafkaHeaders;
import org.springframework.messaging.handler.annotation.Header;
import org.springframework.stereotype.Component;

import static com.javarush.zdanovskih.constant.Topics.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublisherCreatedConsumer {

    private final PublisherCacheRepository repository;

    @KafkaListener(topics = PUBLISHER_CREATED)
    @Transactional
    public void handle(PublisherCreatedEvent event, @Header(KafkaHeaders.OFFSET) Long offset) {
        if (!repository.existsById(event.getId())) {
            repository.save(
                    new PublisherCache(event.getId(), event.getName(), event.getSite()));
            log.info("Publisher created event received: {} at offset {}", event, offset);
        } else {
            log.info("Publisher already exists: {}", event);
        }
    }

    @KafkaListener(topics = PUBLISHER_UPDATED)
    @Transactional
    public void handle(PublisherUpdatedEvent event, @Header(KafkaHeaders.OFFSET) Long offset) {
        if (repository.existsById(event.getId())) {
            log.info("Publisher updated event received: {} at offset {}", event, offset);
        } else {
            log.info("Publisher NOT exists: {}", event);
        }
        repository.save(
                new PublisherCache(event.getId(), event.getName(), event.getSite()));
    }



}