package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookPostDto;
import ru.otus.hw.services.BookService;

@Controller
@RequiredArgsConstructor
public class BookController {
    private final BookService bookService;

    @GetMapping("/")
    public String listBooks(Model model) {
        model.addAttribute("books", bookService.findAll());
        return "books";
    }

    @GetMapping("/edit")
    public String editBook(@RequestParam("id") Integer id, Model model) {
        BookDto byId = bookService.findById(id).orElseThrow(() -> new EntityNotFoundException("No book"));
        model.addAttribute("book", byId);
        return "edit";
    }

    @PostMapping("/edit")
    public String editBook(@ModelAttribute("book") BookPostDto book) {
        bookService.update(
            book.getId(),
            book.getTitle(),
            book.getAuthorId(),
            book.getGenreId()
        );
        return "redirect:/";
    }

    @GetMapping("/add")
    public String addBook(Model model) {
        model.addAttribute("book", new BookPostDto(0L, "some title", 1, 1));
        return "add";
    }

    @PostMapping("/add")
    public String addBook(@ModelAttribute("book") BookPostDto book) {
        bookService.insert(
            book.getTitle(),
            book.getAuthorId(),
            book.getGenreId()
        );
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam("id") Integer id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
