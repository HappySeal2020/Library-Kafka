package com.javarush.zdanovskih.book_service.controller;
import com.javarush.zdanovskih.book_service.BookServiceApplication;
import com.javarush.zdanovskih.book_service.cache.PublisherCache;
import com.javarush.zdanovskih.book_service.repository.PublisherCacheRepository;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.javarush.zdanovskih.constant.Mapping.REST_PUBLISHER_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = BookServiceApplication.class)
public class PublisherCacheIntegrationTest {


    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private PublisherCacheRepository publisherCacheRepository;

    @Test
    void shouldFailOnDeletePublisherLinkedRecords() throws Exception {
        PublisherCache publisherCache = publisherCacheRepository
                .findPublishersWithBooks()
                .getFirst();
        mockMvc.perform(delete(REST_PUBLISHER_PATH+"/"+publisherCache.getId()))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void shouldDeleteNotExistingPublisher() throws Exception {
        long id = Long.MAX_VALUE;

        mockMvc.perform(delete(REST_PUBLISHER_PATH+"/"+id))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string("Data not found"));
    }
}
