package com.javarush.zdanovskih.book_service.kafka;
import com.javarush.zdanovskih.events.PublisherDeletedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.javarush.zdanovskih.constant.Topics.*;

@Service
@RequiredArgsConstructor
@Slf4j
public class PublisherDeletedProducer {
    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPublisherDeleted(Long id) {

        PublisherDeletedEvent event = new PublisherDeletedEvent(id);
        kafkaTemplate.send(PUBLISHER_DELETED, event)
                .whenComplete((result, ex) -> {
                    if (ex != null) {
                        log.error("Failed to send publisher delete event {}", event, ex);
                    } else {
                        log.info("Publisher delete event sent successfully {}", event);
                    }
                });
    }
}
