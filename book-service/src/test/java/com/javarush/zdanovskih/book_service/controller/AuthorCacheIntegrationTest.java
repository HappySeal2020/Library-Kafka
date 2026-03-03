package com.javarush.zdanovskih.book_service.controller;
import com.javarush.zdanovskih.book_service.BookServiceApplication;
import com.javarush.zdanovskih.book_service.cache.AuthorCache;
import com.javarush.zdanovskih.book_service.entity.Book;
import com.javarush.zdanovskih.book_service.repository.AuthorCacheRepository;
import com.javarush.zdanovskih.book_service.service.BookService;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

import static com.javarush.zdanovskih.constant.Mapping.REST_AUTHOR_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@AutoConfigureMockMvc(addFilters = false)
@SpringBootTest(classes = BookServiceApplication.class)
public class AuthorCacheIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private AuthorCacheRepository authorCacheRepository;

    @Test
    void shouldFailOnDeleteAuthorLinkedRecords() throws Exception {
        AuthorCache authorCache = authorCacheRepository
                .findAuthorsWithBooks()
                .getFirst();
        mockMvc.perform(delete(REST_AUTHOR_PATH+"/"+authorCache.getId()))
                .andDo(print())
                .andExpect(status().isConflict());
    }

    @Test
    void shouldDeleteNotExistingAuthor() throws Exception {
        Long id = Long.MAX_VALUE;

        mockMvc.perform(delete(REST_AUTHOR_PATH+"/"+id))
                .andExpect(status().isBadRequest())
                .andExpect(content()
                        .string("Data not found"));
        //verify(authorCacheService, times(1)).deleteAuthorById(id);
    }
}
