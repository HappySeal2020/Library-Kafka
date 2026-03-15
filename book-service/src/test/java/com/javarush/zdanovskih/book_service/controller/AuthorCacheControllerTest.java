package com.javarush.zdanovskih.book_service.controller;

import com.javarush.zdanovskih.book_service.kafka.AuthorDeletedProducer;
import com.javarush.zdanovskih.book_service.repository.AuthorCacheRepository;
import com.javarush.zdanovskih.book_service.service.AuthorCacheService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import static com.javarush.zdanovskih.constant.Mapping.REST_AUTHOR_PATH;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(controllers = AuthorCacheController.class)
@AutoConfigureMockMvc(addFilters = false)
public class AuthorCacheControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    AuthorCacheRepository authorCacheRepository;

    @MockitoBean
    private AuthorCacheService authorCacheService;

    @MockitoBean
    AuthorDeletedProducer authorDeletedProducer;

    @Test
    void shouldDeleteExistingAuthorAndSendEvent() throws Exception {
        Long id = 1L;

        doNothing().when(authorCacheService).deleteAuthorById(id);
        mockMvc.perform(delete(REST_AUTHOR_PATH+"/"+id))
                .andExpect(status().isNoContent());
        verify(authorCacheService, times(1)).deleteAuthorById(id);
    }


}
