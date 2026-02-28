package com.javarush.zdanovskih.publisher_service.kafka;

import com.javarush.zdanovskih.events.PublisherDeletedEvent;
import com.javarush.zdanovskih.publisher_service.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.javarush.zdanovskih.constant.Topics.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class PublisherDeletedConsumer {
    private final PublisherRepository repository;

    @KafkaListener(topics = PUBLISHER_DELETED)
    @Transactional
    public void handle(PublisherDeletedEvent event) {
        log.info("Publisher delete event received {}", event);
        if(repository.existsById(event.getId())) {
            repository.deleteById(event.getId());
        }
    }
}
