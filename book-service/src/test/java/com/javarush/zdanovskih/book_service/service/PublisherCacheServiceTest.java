package com.javarush.zdanovskih.book_service.service;
import com.javarush.zdanovskih.book_service.cache.PublisherCache;
import com.javarush.zdanovskih.book_service.kafka.PublisherDeletedProducer;
import com.javarush.zdanovskih.book_service.repository.PublisherCacheRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;

import java.util.Optional;

import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

@SpringBootTest
//@ExtendWith(MockitoExtension.class)
public class PublisherCacheServiceTest {
    @MockitoBean
    private PublisherCacheRepository publisherCacheRepository;

    @MockitoBean
    private PublisherDeletedProducer publisherDeletedProducer;

    @Autowired
    PublisherCacheService publisherCacheService;

    @Test
    void shouldDeletePublisherAndSendEvent(){
        PublisherCache publisherCache = new PublisherCache(1L, "Test publisher", "www.test-site.com");
        when(publisherCacheRepository.findById(1L))
                .thenReturn(Optional.of(publisherCache));
        publisherCacheService.deletePublisherById(1L);
        verify(publisherCacheRepository).delete(publisherCache);
        verify(publisherDeletedProducer).sendPublisherDeleted(1L);
    }
}
