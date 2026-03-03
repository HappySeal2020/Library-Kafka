package com.javarush.zdanovskih.publisher_service.service;

import com.javarush.zdanovskih.publisher_service.entity.Publisher;
import com.javarush.zdanovskih.publisher_service.kafka.PublisherEventProducer;
import com.javarush.zdanovskih.publisher_service.repository.PublisherRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class PublisherService {

    private final PublisherRepository repository;
    private final PublisherEventProducer producer;

    public Publisher create(String name, String site) {
        log.info("Creating publisher {}...", name);
        Publisher publisher = repository.save(new Publisher(null, name, site));
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                producer.sendPublisherCreated(publisher);
                log.info("Created publisher {} was sent", publisher);
            }
        });
        return publisher;
    }

    public Publisher update(Publisher publisher) {
        log.info("Updating publisher {}...", publisher);
        Publisher savedPublisher = repository.save(publisher);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                producer.sendPublisherUpdated(publisher);
                log.info("Updated author {} was sent", publisher);
            }
        });
        return savedPublisher;
    }
}