package com.javarush.zdanovskih.publisher_service.kafka;

import com.javarush.zdanovskih.events.PublisherUpdatedEvent;
import com.javarush.zdanovskih.publisher_service.entity.Publisher;
import com.javarush.zdanovskih.events.PublisherCreatedEvent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.kafka.core.KafkaTemplate;
import org.springframework.stereotype.Service;

import static com.javarush.zdanovskih.constant.Topics.*;

@Slf4j
@Service
@RequiredArgsConstructor
public class PublisherEventProducer {

    private final KafkaTemplate<String, Object> kafkaTemplate;

    public void sendPublisherCreated(Publisher publisher) {
        PublisherCreatedEvent event = new PublisherCreatedEvent(publisher.getId(), publisher.getName(), publisher.getSite());
        kafkaTemplate.send(PUBLISHER_CREATED, event)
                .whenComplete((result, ex) ->{
                    if(ex != null){
                        log.error("Failed to send publisher create event{}", event, ex);
                    } else {
                        log.info("Publisher create event sent successfully {}", event);
                    }
                });
    }

    public void sendPublisherUpdated(Publisher publisher) {
        PublisherUpdatedEvent event = new PublisherUpdatedEvent(publisher.getId(), publisher.getName(), publisher.getSite());
        kafkaTemplate.send(PUBLISHER_UPDATED, event)
                .whenComplete((result, ex) ->{
                    if(ex != null){
                        log.error("Failed to send publisher update event {}", event, ex);
                    } else {
                        log.info("Publisher update event sent successfully {}", event);
                    }
                });
    }
}
