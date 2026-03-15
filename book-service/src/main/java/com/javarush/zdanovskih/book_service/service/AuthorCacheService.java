package com.javarush.zdanovskih.book_service.service;
import com.javarush.zdanovskih.book_service.cache.AuthorCache;
import com.javarush.zdanovskih.book_service.exception.NotFoundException;
import com.javarush.zdanovskih.book_service.kafka.AuthorDeletedProducer;
import com.javarush.zdanovskih.book_service.repository.AuthorCacheRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.transaction.support.TransactionSynchronization;
import org.springframework.transaction.support.TransactionSynchronizationManager;

@Slf4j
@Service
@RequiredArgsConstructor
@Transactional(readOnly = true)
public class AuthorCacheService {

    private final AuthorCacheRepository authorCacheRepository;
    private final AuthorDeletedProducer authorDeletedProducer;

    //READ
    public AuthorCache getById(Long id) {
        return authorCacheRepository.findById(id)
                .orElseThrow(() ->
                        new NotFoundException("Author not found in cache id=" + id));
    }

    //DELETE
    @Transactional
    public void deleteAuthorById(Long id) {
        AuthorCache authorCache = getById(id);
        log.info("Try to delete author {} in cache...", authorCache);
        authorCacheRepository.delete(authorCache);
        //authorCacheRepository.flush();
        TransactionSynchronizationManager.registerSynchronization(new TransactionSynchronization() {
            @Override
            public void afterCommit() {
                authorDeletedProducer.sendAuthorDeleted(id);
            }
        });
    }

}
