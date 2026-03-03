package com.javarush.zdanovskih.book_service.controller;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.zdanovskih.book_service.BookServiceApplication;
import com.javarush.zdanovskih.book_service.cache.AuthorCache;
import com.javarush.zdanovskih.book_service.cache.PublisherCache;
import com.javarush.zdanovskih.book_service.entity.Book;
import com.javarush.zdanovskih.book_service.repository.AuthorCacheRepository;
import com.javarush.zdanovskih.book_service.repository.PublisherCacheRepository;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.javarush.zdanovskih.constant.Mapping.REST_BOOK_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = BookServiceApplication.class)
public class BookIntegrationTest {

    @Autowired
    PublisherCacheRepository publisherCacheRepository;

    @Autowired
    AuthorCacheRepository authorCacheRepository;

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFailOnDuplicateName() throws Exception {
        AuthorCache authorCache = new AuthorCache(0L, "Popular Author");
        AuthorCache savedAuthorCache = authorCacheRepository.save(authorCache);
        PublisherCache publisherCache = new PublisherCache(0L, "FastPrint","www.fastprint.com");
        PublisherCache savedPublisherCache = publisherCacheRepository.save(publisherCache);

        //BookCreationDto dto = new BookCreationDto("Adventures of Super Hero", List.of(author), 2000, publisher, "bbk", "isbn", 500);
        Book book = new Book(0L, "Adventures of Super Hero", List.of(savedAuthorCache), 2000,
                savedPublisherCache, "bbk", "isbn", 500);

        mockMvc.perform(post(REST_BOOK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isCreated());

        mockMvc.perform(post(REST_BOOK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(book)))
                .andExpect(status().isConflict());
    }
}
