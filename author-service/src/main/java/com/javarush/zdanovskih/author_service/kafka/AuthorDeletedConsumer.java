package com.javarush.zdanovskih.author_service.kafka;

import com.javarush.zdanovskih.author_service.repository.AuthorRepository;
import com.javarush.zdanovskih.events.AuthorDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.annotation.KafkaListener;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Transactional;

import static com.javarush.zdanovskih.constant.Topics.*;

@Slf4j
@Component
@RequiredArgsConstructor
public class AuthorDeletedConsumer {
    private final AuthorRepository repository;


    @KafkaListener(topics = AUTHOR_DELETED)
    @Transactional
    public void handle(AuthorDeletedEvent event) {
        log.info("Author delete event received {}", event);
        if(repository.existsById(event.getId())) {
            repository.deleteById(event.getId());
        }
    }
}
