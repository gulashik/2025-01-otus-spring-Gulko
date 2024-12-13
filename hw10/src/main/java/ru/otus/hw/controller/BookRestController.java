package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.services.BookService;

import java.util.List;

@RestController
@RequestMapping(path = "api/v1")
@RequiredArgsConstructor
public class BookRestController {
    private final BookService bookService;

    @GetMapping("/books")
    public List<BookDto> listBooks() {
        return bookService.findAll();
    }
}
