package com.javarush.zdanovskih.book_service.controller;

import com.javarush.zdanovskih.book_service.service.PublisherCacheService;
import org.junit.jupiter.api.Test;

import static com.javarush.zdanovskih.constant.Mapping.REST_PUBLISHER_PATH;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

@WebMvcTest(controllers = PublisherCacheController.class)
@AutoConfigureMockMvc(addFilters = false)
public class PublisherCacheControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private PublisherCacheService publisherCacheService;

    @Test
    void shouldDeleteExistingPublisher() throws Exception {
        Long id = 1L;
        doNothing().when(publisherCacheService).deletePublisherById(id);
        mockMvc.perform(delete(REST_PUBLISHER_PATH+"/"+id))
                .andExpect(status().isNoContent());
        verify(publisherCacheService, times(1)).deletePublisherById(id);
    }

}
