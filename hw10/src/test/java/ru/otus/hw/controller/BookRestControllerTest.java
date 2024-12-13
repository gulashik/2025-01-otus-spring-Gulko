package ru.otus.hw.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.objects.TestObjects.getDbBooks;

@WebMvcTest(BookRestController.class)
class BookRestControllerTest {

    private final List<BookDto> dbBookDtos = getDbBooks();
    @Autowired
    private MockMvc mockMvc;

    @MockBean
    private BookService bookService;

    @Autowired
    private ObjectMapper mapper;

    @Test
    void getBook() throws Exception {
        BookDto expectedBook = dbBookDtos.get(0);

        when(bookService.findAll()).thenReturn(List.of(expectedBook));

        mockMvc.perform(
                get("/api/v1/books")
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType("application/json"))
            .andExpect(content().json(mapper.writeValueAsString(List.of(expectedBook))));
    }
}