package ru.otus.hw.controller;

import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;
import static ru.otus.hw.objects.TestObjects.getDbBooks;

@WebMvcTest(BookController.class)
class BookControllerTest {

    private final List<BookDto> dbBookDtos = getDbBooks();
    @Autowired
    private MockMvc mockMvc;
    @MockBean
    private BookService bookService;

    @Test
    void getBook() throws Exception {
        BookDto expectedBook = dbBookDtos.get(0);

        when(bookService.findAll()).thenReturn(List.of(expectedBook));

        MvcResult result = mockMvc.perform(
                get("/")
            )
            .andExpect(status().isOk())
            .andExpect(content().contentType("text/html;charset=UTF-8"))
            .andReturn();

        List<BookDto> books = (List<BookDto>) result.getModelAndView().getModel().get("books");
        assertThat(books).isNotNull();
        assertThat(books.size()).isEqualTo(1);

        assertThat(books.get(0)).isEqualTo(expectedBook);
    }

    @Test
    void editBook() throws Exception {
        BookDto expectedBook = dbBookDtos.get(0);

        when(bookService.findById(expectedBook.getId())).thenReturn(Optional.of(expectedBook));

        mockMvc.perform(
            get("/edit")
            .param("id", String.valueOf(expectedBook.getId()))
        )
        .andExpect(status().isOk())
        .andExpect(content().contentType("text/html;charset=UTF-8"));

        Mockito.verify(bookService, Mockito.times(1)).findById(expectedBook.getId());
    }
}