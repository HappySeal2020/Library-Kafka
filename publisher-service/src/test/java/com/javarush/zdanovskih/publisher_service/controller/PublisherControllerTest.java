package com.javarush.zdanovskih.publisher_service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.zdanovskih.publisher_service.entity.Publisher;
import com.javarush.zdanovskih.publisher_service.repository.PublisherRepository;
import com.javarush.zdanovskih.publisher_service.service.PublisherService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static com.javarush.zdanovskih.constant.Mapping.REST_PUBLISHER_PATH;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;

@WebMvcTest(controllers = PublisherController.class)

public class PublisherControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private PublisherRepository publisherRepository;

    @MockitoBean
    private PublisherService publisherService;

    @BeforeEach
    void setup() {
        when(publisherRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void shouldReturnAllPublishers() throws Exception {
        //Create test data
        List<Publisher> publishers = List.of(new Publisher(1L, "Fast print", "www.fast-print.com"));

        when(publisherRepository.findAll(any(Specification.class))).thenReturn(publishers);
        mockMvc.perform(get(REST_PUBLISHER_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Fast print"))
                .andExpect(jsonPath("$[0].site").value("www.fast-print.com"));
    }

    @Test
    void shouldCreateNewPublisher() throws Exception {
        Publisher savedPublisher = new Publisher(1L, "Test Publisher", "www.publisher.com");
        when(publisherService.create(any(String.class), any(String.class) )).thenReturn(savedPublisher);
        mockMvc.perform(post(REST_PUBLISHER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "name": "Test Publisher",
                  "site": "www.publisher.com"
                }
            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Publisher"))
                .andExpect(jsonPath("$.site").value("www.publisher.com"));
    }

    @Test
    void shouldUpdateExistingPublisher() throws Exception {
        Publisher savedPublisher = new Publisher(1L, "New Publisher", "www.publisher.com");
        //when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);
        when (publisherService.update(any(Publisher.class))).thenReturn(savedPublisher);
        mockMvc.perform(put(REST_PUBLISHER_PATH+"/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "id": 1,
                  "name": "New Publisher",
                  "site": "www.publisher.com"
                }
            """))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New Publisher"))
                .andExpect(jsonPath("$.site").value("www.publisher.com"));
    }

    // No delete via REST in Publisher-service

    @Test
    void shouldReturnValidationError() throws Exception {
        Publisher requestPublisher = new Publisher(null, "", "www.publisher.com");

        mockMvc.perform(post(REST_PUBLISHER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestPublisher)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name")
                        .value("Publisher name is mandatory field"));
    }

    @Test
    void shouldReturnConflictWhenNameIsNotUnique() throws Exception {

        Publisher requestPublisher = new Publisher(0L, "Super-Print", "www.super-print.com");

        //when(publisherRepository.save(any()))
        //        .thenThrow(new DataIntegrityViolationException("Duplicate entry"));
        when(publisherService.create(any(String.class), any(String.class) ))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        mockMvc.perform(post( REST_PUBLISHER_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestPublisher)))
                .andExpect(status().isConflict())
                .andExpect(content()
                        .string("Data already exists"));
    }

    @Test
    void shouldFilterPublisherByName() throws Exception {
        List<Publisher> publishers = List.of(new Publisher(1L, "Super-Print", "www.super-print.com"));

        when(publisherRepository.findAll(any(Specification.class)))
                .thenReturn(publishers);
        mockMvc.perform(get( REST_PUBLISHER_PATH)
                        .param("name", "push"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Super-Print"))
                .andExpect(jsonPath("$[0].site").value("www.super-print.com"));
    }



}
