package com.javarush.zdanovskih.book_service.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.javarush.zdanovskih.book_service.cache.AuthorCache;
import com.javarush.zdanovskih.book_service.cache.PublisherCache;
import com.javarush.zdanovskih.book_service.entity.Book;
import com.javarush.zdanovskih.book_service.repository.BookRepository;
import com.javarush.zdanovskih.book_service.service.BookService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;

import static com.javarush.zdanovskih.constant.Mapping.REST_BOOK_PATH;
import static org.mockito.Mockito.*;

import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(controllers = BookController.class)
//@SpringBootTest
@AutoConfigureMockMvc(addFilters = false)
public class BookControllerTest {
    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockitoBean
    private BookRepository bookRepository;

    @MockitoBean
    private BookService bookService;

    //@BeforeEach
    //void setup() {
    //    when(bookRepository.save(any())).thenAnswer(i -> i.getArgument(0));
    //}

    @Test
    void shouldReturnAllBooks() throws Exception {
        //Create test data
        AuthorCache authorCache = new AuthorCache(0L,"Test author");
        PublisherCache publisherCache = new PublisherCache(0L,"Test publisher","Test site");

        List<Book> books = List.of(new Book(1L, "First book", List.of(authorCache), 2000,
                publisherCache, "bbk1", "isbn1", 1000));

        when(bookService.getAllBooks(null, null, null, null, null,
                null, null, null, null)).thenReturn(books);
        mockMvc.perform(get(REST_BOOK_PATH))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].name").value("First book"));
    }

    @Test
    void shouldCreateNewBook() throws Exception {
        AuthorCache authorCache = new AuthorCache(1L,"Test author");
        PublisherCache publisherCache = new PublisherCache(1L,"Test publisher","Test site");
        Book savedBook = new Book(1L, "Test Book",List.of(authorCache), 2000, publisherCache,
                "bbk", "isbn", 500);
        when(bookService.create(any(Book.class))).thenReturn(savedBook);
        mockMvc.perform(post(REST_BOOK_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "name":"Test Book",
                  "authors": [
                     {
                        "id": 1,
                        "name":"Test Author"
                     }
                  ],
                  "printYear": 2000,
                  "publisher": {
                       "id": 1,
                       "name":"Test Publisher",
                       "site": "Test site"
                     },
                  "bbk":"bbk",
                  "isbn": "isbn",
                  "pages":500
                }
            """))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Book"));
    }

    @Test
    void shouldUpdateExistingBook() throws Exception {
        AuthorCache authorCache = new AuthorCache(1L,"Test author");
        PublisherCache publisherCache = new PublisherCache(1L,"Test publisher","Test site");
        Book savedBook = new Book(1L, "Test Book update",List.of(authorCache), 2000, publisherCache,
                "bbk", "isbn", 500);
        when(bookService.create(any(Book.class))).thenReturn(savedBook);
        mockMvc.perform(put(REST_BOOK_PATH+"/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "id": 1,
                  "name":"Test Book update",
                  "authors": [
                     {
                        "id": 1,
                        "name":"Test Author"
                     }
                  ],
                  "printYear": 2000,
                  "publisher": {
                       "id": 1,
                       "name":"Test Publisher",
                       "site": "Test site"
                     },
                  "bbk":"bbk",
                  "isbn": "isbn",
                  "pages":500
                }
            """))
                .andExpect(status().isAccepted())
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("Test Book update"));
    }

    @Test
    void shouldDeleteExistingBook() throws Exception {
        Long id = 1L;
        doNothing().when(bookService).deleteBookById(id);
        mockMvc.perform(delete(REST_BOOK_PATH+"/"+id))
                .andExpect(status().isNoContent());
        verify(bookService, times(1)).deleteBookById(id);
    }


    @Test
    void shouldReturnValidationErrorOnSmallPrintYear() throws Exception {
        AuthorCache authorCache = new AuthorCache(1L,"Test author");
        PublisherCache publisherCache = new PublisherCache(1L,"Test publisher","Test site");
        Book savedBook = new Book(1L, "Test Book",List.of(authorCache), 1000, publisherCache,
                "bbk", "isbn", 500);
        when(bookService.create(any(Book.class))).thenReturn(savedBook);
        mockMvc.perform(put(REST_BOOK_PATH+"/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "id": 1,
                  "name":"Test Book",
                  "authors": [
                     {
                        "id": 1,
                        "name":"Test Author"
                     }
                  ],
                  "printYear": 1000,
                  "publisher": {
                       "id": 1,
                       "name":"Test Publisher",
                       "site": "Test site"
                     },
                  "bbk":"bbk",
                  "isbn": "isbn",
                  "pages":500
                }
            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.printYear").value("Print year must be greater 1900"));
    }

    @Test
    void shouldReturnValidationErrorOnPages() throws Exception {
        AuthorCache authorCache = new AuthorCache(1L,"Test author");
        PublisherCache publisherCache = new PublisherCache(1L,"Test publisher","Test site");
        Book savedBook = new Book(1L, "Test Book",List.of(authorCache), 3000, publisherCache,
                "bbk", "isbn", -1);
        when(bookService.create(any(Book.class))).thenReturn(savedBook);
        mockMvc.perform(put(REST_BOOK_PATH+"/1")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content("""
                {
                  "id": 1,
                  "name":"Test Book",
                  "authors": [
                     {
                        "id": 1,
                        "name":"Test Author"
                     }
                  ],
                  "printYear": 3000,
                  "publisher": {
                       "id": 1,
                       "name":"Test Publisher",
                       "site": "Test site"
                     },
                  "bbk":"bbk",
                  "isbn": "isbn",
                  "pages":-1
                }
            """))
                .andExpect(status().isBadRequest())
                .andExpect(jsonPath("$.pages").value("Number of pages must be positive"));
    }

    @Test
    void shouldFilterBooksByName() throws Exception {
        AuthorCache authorCache = new AuthorCache(1L,"Famous author");
        PublisherCache publisherCache = new PublisherCache(1L,"Read us print","read-us-print.com");
        List<Book> books = List.of(new Book(1L, "Super Book",List.of(authorCache), 1970,
                publisherCache, "bbk", "isbn", 300));

        when(bookService.getAllBooks(eq("Super"), any(), any(),any(), any(), any(), any(), any(), any()))
                .thenReturn(books);
        mockMvc.perform(get(REST_BOOK_PATH)
                        .param("name", "Super"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Super Book"));
    }

    @Test
    void shouldFilterBooksByAuthor() throws Exception {
        AuthorCache authorCache = new AuthorCache(1L,"Famous author");
        PublisherCache publisherCache = new PublisherCache(1L,"Read us print","read-us-print.com");
        List<Book> books = List.of(new Book(1L, "Super Book",List.of(authorCache), 1970,
                publisherCache, "bbk", "isbn", 300));

        when(bookService.getAllBooks(any(), eq("amous"), any(),any(), any(), any(), any(), any(), any()))
                .thenReturn(books);
        mockMvc.perform(get(REST_BOOK_PATH)
                        .param("author", "amous"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Super Book"));
    }


    @Test
    void shouldFilterBooksByPrintYear() throws Exception {
        AuthorCache authorCache = new AuthorCache(1L,"Famous author");
        PublisherCache publisherCache = new PublisherCache(1L,"Read us print","read-us-print.com");
        List<Book> books = List.of(new Book(1L, "Super Book",List.of(authorCache), 1970,
                publisherCache, "bbk", "isbn", 300));

        when(bookService.getAllBooks(any(), any(), eq(1970), any(),any(), any(), any(), any(), any() ))
                .thenReturn(books);
        mockMvc.perform(get(REST_BOOK_PATH)
                        .param("printYearFrom", "1970"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Super Book"));
    }

    @Test
    void shouldFilterBooksByPrintYearBetween() throws Exception {
        AuthorCache authorCache = new AuthorCache(1L,"Famous author");
        PublisherCache publisherCache = new PublisherCache(1L,"Read us print","read-us-print.com");
        List<Book> books = List.of(new Book(1L, "Super Book",List.of(authorCache), 2000,
                        publisherCache, "bbk", "isbn", 300),
                new Book(2L, "Super Book. Continue.",List.of(authorCache), 2010, publisherCache,
                        "bbk", "isbn", 300),
                new Book(3L, "Super Book v2",List.of(authorCache), 2005, publisherCache, "bbk",
                        "isbn", 300));

        when(bookService.getAllBooks(any(), any(), eq(2000), eq(2010),any(), any(), any(), any(), any() ))
                .thenReturn(books);
        mockMvc.perform(get(REST_BOOK_PATH)
                        .param("printYearFrom", "2000")
                        .param("printYearTo", "2010"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }

    @Test
    void shouldFilterBooksByPublisher() throws Exception {
        AuthorCache authorCache = new AuthorCache(1L,"Famous author");
        PublisherCache publisherCache = new PublisherCache(1L,"Read us print","read-us-print.com");
        List<Book> books = List.of(new Book(1L, "Super Book",List.of(authorCache), 1970,
                publisherCache, "bbk", "isbn", 300));

        when(bookService.getAllBooks(any(), any(), any(), any(),eq("print"), any(), any(), any(), any() ))
                .thenReturn(books);
        mockMvc.perform(get(REST_BOOK_PATH)
                        .param("publisher", "print"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Super Book"));
    }

    @Test
    void shouldFilterBooksByBbk() throws Exception {
        AuthorCache authorCache = new AuthorCache(1L,"Famous author");
        PublisherCache publisherCache = new PublisherCache(1L,"Read us print","read-us-print.com");
        List<Book> books = List.of(new Book(1L, "Super Book",List.of(authorCache), 1970,
                publisherCache, "2343167", "isbn", 300));

        when(bookService.getAllBooks(any(), any(), any(), any(), any(),eq("3167"), any(), any(), any() ))
                .thenReturn(books);
        mockMvc.perform(get(REST_BOOK_PATH)
                        .param("bbk", "3167"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Super Book"));
    }

    @Test
    void shouldFilterBooksByIsbn() throws Exception {
        AuthorCache authorCache = new AuthorCache(1L,"Famous author");
        PublisherCache publisherCache = new PublisherCache(1L,"Read us print","read-us-print.com");
        List<Book> books = List.of(new Book(1L, "Super Book",List.of(authorCache), 1970,
                publisherCache, "bbk", "8-800-200-8002", 300));

        when(bookService.getAllBooks(any(), any(), any(), any(), any(), any(), eq("8002"),any(), any() ))
                .thenReturn(books);
        mockMvc.perform(get(REST_BOOK_PATH)
                        .param("isbn", "8002"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Super Book"));
    }

    @Test
    void shouldFilterBooksByPages() throws Exception {
        AuthorCache authorCache = new AuthorCache(1L,"Famous author");
        PublisherCache publisherCache = new PublisherCache(1L,"Read us print","read-us-print.com");
        List<Book> books = List.of(new Book(1L, "Super Book",List.of(authorCache), 1970,
                publisherCache, "bbk", "isbn", 314));

        when(bookService.getAllBooks(any(), any(), any(), any(), any(), any(), any(), eq(314), any() ))
                .thenReturn(books);
        mockMvc.perform(get(REST_BOOK_PATH)
                        .param("pagesFrom", "314"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].name").value("Super Book"));
    }

    @Test
    void shouldFilterBooksByPagesBetween() throws Exception {
        AuthorCache authorCache = new AuthorCache(1L,"Famous author");
        PublisherCache publisherCache = new PublisherCache(1L,"Read us print","read-us-print.com");
        List<Book> books = List.of(new Book(1L, "Super Book",List.of(authorCache), 2000,
                        publisherCache, "bbk", "isbn", 300),
                new Book(2L, "Super Book. Continue.",List.of(authorCache), 2010,
                        publisherCache, "bbk", "isbn", 700),
                new Book(3L, "Super Book v2",List.of(authorCache), 2005,
                        publisherCache, "bbk", "isbn", 500));

        when(bookService.getAllBooks(any(), any(), any(), any(), any(), any(), any(), eq(300) , eq(700)))
                .thenReturn(books);
        mockMvc.perform(get(REST_BOOK_PATH)
                        .param("pagesFrom", "300")
                        .param("pagesTo", "700"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(3));
    }
}
