package ru.otus.hw.commands;

import lombok.RequiredArgsConstructor;
import org.springframework.shell.standard.ShellComponent;
import org.springframework.shell.standard.ShellMethod;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.converters.BookConverter;
import ru.otus.hw.services.BookService;

import java.util.stream.Collectors;

@SuppressWarnings({"SpellCheckingInspection", "unused"})
@RequiredArgsConstructor
@ShellComponent
public class BookCommands {

    private final BookService bookService;

    private final BookConverter bookConverter;

    @ShellMethod(value = "Find all books", key = "ab")
    public String findAllBooks() {
        return bookService.findAll()
            .map(bookConverter::bookToString)
            .collect(Collectors.joining("," + System.lineSeparator()))
            .block();
    }

    // bbid 1
    @ShellMethod(value = "Find book by id", key = "bbid")
    public String findBookById(String id) {
        return bookService.findById(id)
            .map(bookConverter::bookToString)
            .switchIfEmpty(Mono.just("Book with id %s not found".formatted(id)))
            .block();
    }

    // bins newBook 1 1
    @ShellMethod(value = "Insert book", key = "bins")
    public String insertBook(String title, String authorId, String genreId) {
        return bookService
            .insert(title, authorId, genreId)
            .map(bookConverter::bookToString)
            .block();
    }

    // bupd 3 newTitle 3 2
    @ShellMethod(value = "Update book", key = "bupd")
    public String updateBook(String id, String title, String authorId, String genreId) {
        return bookService
            .update(id, title, authorId, genreId)
            .map(bookConverter::bookToString)
            .block();
    }

    // bdel 3 и ab удалили
    @ShellMethod(value = "Delete book by id", key = "bdel")
    public void deleteBook(String id) {
        bookService.deleteById(id);
    }
}
