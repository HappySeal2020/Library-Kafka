package com.javarush.zdanovskih.author_service.service;

import com.javarush.zdanovskih.author_service.entity.Author;
import com.javarush.zdanovskih.author_service.kafka.AuthorEventProducer;
import com.javarush.zdanovskih.author_service.repository.AuthorRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import lombok.extern.slf4j.Slf4j;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional
public class AuthorService {

    private final AuthorRepository repository;
    private final AuthorEventProducer producer;

    public Author create(String name) {
            log.info("Creating author {}...", name);
            Author author = repository.save(new Author(null, name));
            TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
                @Override
                public void afterCommit() {
                    producer.sendAuthorCreated(author);
                    log.info("Created author {} was sent", author);
                }
            });
            return author;
    }

    //delete author -- from book

    public void update(Author author) {
        log.info("Updating author {}...", author);
        repository.save(author);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                producer.sendAuthorUpdated(author);
                log.info("Updated author {} was sent", author);
            }
        });
    }
}