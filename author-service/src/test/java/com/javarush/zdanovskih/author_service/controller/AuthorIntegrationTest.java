package com.javarush.zdanovskih.author_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.zdanovskih.author_service.entity.Author;
import jakarta.transaction.Transactional;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static com.javarush.zdanovskih.constant.Mapping.REST_AUTHOR_PATH;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@Transactional
@AutoConfigureMockMvc(addFilters = false)

@SpringBootTest
public class AuthorIntegrationTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @Test
    void shouldFailOnDuplicateName() throws Exception {

        Author author = new Author(0L, "Tolstoy");

        mockMvc.perform(post( REST_AUTHOR_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isCreated());

        mockMvc.perform(post( REST_AUTHOR_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(author)))
                .andExpect(status().isConflict());
    }
}
