package ru.otus.hw.objects;

import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.dto.GenreDto;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;
import java.util.stream.IntStream;

public class TestObjects {


    public static List<AuthorDto> getDbAuthors() {
        return IntStream.range(1, 4).boxed()
            .map(id -> new AuthorDto(String.valueOf(id), "Author_" + id))
            .toList();
    }

    public static List<GenreDto> getDbGenres() {
        return IntStream.range(1, 4).boxed()
            .map(id -> new GenreDto(String.valueOf(id), "Genre_" + id))
            .toList();
    }

    public static List<BookDto> getDbBooks(List<AuthorDto> dbAuthorDtos, List<GenreDto> dbGenreDtos) {
        return IntStream.range(1, 4).boxed()
            .map(id -> new BookDto(
                String.valueOf(id),
                "BookTitle_" + id, dbAuthorDtos.get(id - 1),
                dbGenreDtos.get(id - 1)
                )
            )
            .toList();
    }

    public static List<BookDto> getDbBooks() {
        return getDbBooks( getDbAuthors(), getDbGenres());
    }

    public static List<CommentDto> getDbComments() {
        Map<String, List<BookDto>> mapBook = getDbBooks()
            .stream()
            .collect(Collectors.groupingBy(BookDto::getId));
        var bookOne = mapBook.get("1").get(0);
        var bookTwo = mapBook.get("2").get(0);

        return List.of(
            new CommentDto("1", "Comment_1_book_1", bookOne),
            new CommentDto("2", "Comment_2_book_1", bookOne),
            new CommentDto("3", "Comment_3_book_1", bookOne),

            new CommentDto("4", "Comment_1_book_2", bookTwo),
            new CommentDto("5", "Comment_2_book_2", bookTwo)
        );
    }
}
