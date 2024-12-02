package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.dto.BookCreateDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookUpdateDto;
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
        BookDto byId = bookService.findById(id).orElseThrow(() -> new NotFoundException("No book"));
        model.addAttribute("book", byId);
        return "edit";
    }

    @PostMapping("/edit")
    public String editBook(@ModelAttribute("book") BookUpdateDto book) {
        bookService.update(book);
        return "redirect:/";
    }

    @GetMapping("/add")
    public String createBook(Model model) {
        model.addAttribute("book", new BookUpdateDto(0L, "some title", 1l, 1l));
        return "add";
    }

    @PostMapping("/add")
    public String createBook(@ModelAttribute("book") BookCreateDto book) {
        bookService.insert(book);
        return "redirect:/";
    }

    @GetMapping("/delete")
    public String deleteBook(@RequestParam("id") Integer id) {
        bookService.deleteById(id);
        return "redirect:/";
    }
}
