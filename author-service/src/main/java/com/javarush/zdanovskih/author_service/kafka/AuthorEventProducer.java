package com.javarush.zdanovskih.author_service.kafka;

import com.javarush.zdanovskih.author_service.entity.Author;
import com.javarush.zdanovskih.events.AuthorCreatedEvent;
import com.javarush.zdanovskih.events.AuthorUpdatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.javarush.zdanovskih.constant.Topics.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendAuthorCreated(Author author) {
        AuthorCreatedEvent event = new AuthorCreatedEvent(author.getId(), author.getName());
        kafkaTemplate.send(AUTHOR_CREATED, event)
        //kafkaTemplate.send(AUTHOR_CREATED, "invalid-json")
                .whenComplete((result, ex) ->{
                    if(ex != null){
                        log.error("Failed to send author create event {}", event, ex);
                    } else {
                        log.info("Author create event sent successfully {}", event);
                    }
                });
    }
    public void sendAuthorUpdated(Author author) {
        AuthorUpdatedEvent event = new AuthorUpdatedEvent(author.getId(), author.getName());
        kafkaTemplate.send(AUTHOR_UPDATED, event)
                .whenComplete((result, ex) ->{
                    if(ex != null){
                        log.error("Failed to send author update event {}", event, ex);
                    } else {
                        log.info("Author update event sent successfully {}", event);
                    }
                });
    }
}