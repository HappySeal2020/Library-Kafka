package com.javarush.zdanovskih.book_service.kafka;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import com.javarush.zdanovskih.events.AuthorDeletedEvent;
import org.springframework.kafka.core.KafkaTemplate;
import static com.javarush.zdanovskih.constant.Topics.AUTHOR_DELETED;

@Slf4j
@Service
@RequiredArgsConstructor
public class AuthorDeletedProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendAuthorDeleted(Long id) {
        AuthorDeletedEvent event = new AuthorDeletedEvent(id);
        kafkaTemplate.send(AUTHOR_DELETED, event)
                .whenComplete((result, ex) ->{
                    if(ex != null){
                        log.error("Failed to send author delete event {}", event, ex);
                    } else {
                        log.info("Author delete event sent successfully {}", event);
                    }
                });
    }
}
