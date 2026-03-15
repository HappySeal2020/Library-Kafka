package com.javarush.zdanovskih.author_service.controller;


import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.zdanovskih.author_service.entity.Author;
import com.javarush.zdanovskih.author_service.repository.AuthorRepository;
import com.javarush.zdanovskih.author_service.service.AuthorService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static com.javarush.zdanovskih.constant.Mapping.REST_AUTHOR_PATH;
import static org.mockito.Mockito.*;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = AuthorController.class)

public class AuthorControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private AuthorRepository authorRepository;

    @MockitoBean
    private AuthorService authorService;

    @BeforeEach
    void setup() {
        when(authorRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    }

    @Test
    void shouldReturnAllAuthors() throws Exception {
        //Create test data
        List<Author> authors = List.of(new Author(1L, "Alexander Volkov"));

        when(authorRepository.findAll(any(Specification.class))).thenReturn(authors);
        mockMvc.perform(get(REST_AUTHOR_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("Alexander Volkov"));
    }

    @Test
    void shouldCreateNewAuthor() throws Exception {
        Author savedAuthor = new Author(1L, "Test Author");
        //when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);
        when(authorService.create(any(String.class) )).thenReturn(savedAuthor);
        mockMvc.perform(post(REST_AUTHOR_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "name":"Test Author"
                }
            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("Test Author"));
    }

    @Test
    void shouldUpdateExistingAuthor() throws Exception {
        Author savedAuthor = new Author(1L, "New Author");
        //when(authorRepository.save(any(Author.class))).thenReturn(savedAuthor);
        when (authorService.update(any(Author.class))).thenReturn(savedAuthor);
        mockMvc.perform(put(REST_AUTHOR_PATH+"/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "id":1,
                  "name":"New Author"
                }
            """))
                .andDo(print())
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(1L))
                .andExpect(jsonPath("$.name").value("New Author"));
    }

    // No delete via REST in Author-service


    @Test
    void shouldReturnValidationError() throws Exception {
        Author requestAuthor = new Author(null, "");

        mockMvc.perform(post(REST_AUTHOR_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestAuthor)))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.name")
                        .value("Author name is mandatory field"));
    }

    @Test
    void shouldReturnConflictWhenNameIsNotUnique() throws Exception {

        Author requestAuthor = new Author(0L, "Tolstoy");

        //when(authorRepository.save(any()))
        when(authorService.create(any(String.class) ))
                .thenThrow(new DataIntegrityViolationException("Duplicate entry"));

        mockMvc.perform(post( REST_AUTHOR_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(requestAuthor)))
                .andExpect(status().isConflict())
                .andExpect(content()
                        .string("Data already exists"));
    }

    @Test
    void shouldFilterAuthorsByName() throws Exception {
        List<Author> authors = List.of(new Author(1L, "Pushkin"));

        when(authorRepository.findAll(any(Specification.class)))
                .thenReturn(authors);
        mockMvc.perform(get( REST_AUTHOR_PATH)
                        .param("name", "push"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Pushkin"));
    }
}
