package ru.otus.hw.objects;

import ru.otus.hw.models.dto.Author;
import ru.otus.hw.models.dto.Book;
import ru.otus.hw.models.dto.Comment;
import ru.otus.hw.models.dto.Genre;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestObjects {


    public static List<Author> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
            .map(id -> new Author(id, "Author_" + id))
            .toList();
    }

    public static List<Genre> getDbGenres() {
        return IntStream.range(1, 4).boxed()
            .map(id -> new Genre(id, "Genre_" + id))
            .toList();
    }

    public static List<Book> getDbBooks(List<Author> dbAuthors, List<Genre> dbGenres) {
        return IntStream.range(1, 4).boxed()
            .map(id -> new Book(id, "BookTitle_" + id, dbAuthors.get(id - 1), dbGenres.get(id - 1)))
            .toList();
    }

    public static List<Book> getDbBooks() {
        return getDbBooks( getDbAuthors(), getDbGenres());
    }

    public static List<Comment> getDbComments() {
        Map<Long, List<Book>> mapBook = getDbBooks()
            .stream()
            .collect(Collectors.groupingBy(Book::getId));
        var bookOne = mapBook.get(1L).get(0);
        var bookTwo = mapBook.get(2L).get(0);

        return List.of(
            new Comment(1L, "Comment_1_book_1", bookOne),
            new Comment(2L, "Comment_2_book_1", bookOne),
            new Comment(3L, "Comment_3_book_1", bookOne),

            new Comment(4L, "Comment_1_book_2", bookTwo),
            new Comment(5L, "Comment_2_book_2", bookTwo)
        );
    }
}
