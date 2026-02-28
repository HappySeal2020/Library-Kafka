package com.javarush.zdanovskih.book_service.service;
import com.javarush.zdanovskih.book_service.cache.PublisherCache;
import com.javarush.zdanovskih.book_service.exception.NotFoundException;
import com.javarush.zdanovskih.book_service.repository.PublisherCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;
import com.javarush.zdanovskih.book_service.kafka.PublisherDeletedProducer;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class PublisherCacheService {
    private final PublisherCacheRepository publisherCacheRepository;
    private final PublisherDeletedProducer publisherDeletedProducer;

    //READ
    public PublisherCache getById(Long id) {
        return publisherCacheRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Publisher not found in cache id=" + id));
    }

    //DELETE
    @Transactional
    public void deletePublisherById(Long id) {
        PublisherCache publisherCache = getById(id);
        log.info("Try to delete publisher {} in cache...", publisherCache);
        publisherCacheRepository.delete(publisherCache);
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                publisherDeletedProducer.sendPublisherDeleted(id);
            }
        });
    }

}
