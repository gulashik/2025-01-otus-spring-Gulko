package ru.otus.hw.services;

import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.BookUpdateDto;

import java.util.List;
import java.util.Optional;

public interface BookService {
    Optional<BookDto> findById(long id);

    List<BookDto> findAll();

    BookDto insert(String title, long authorId, long genreId);

    BookDto update(BookUpdateDto bookUpdateDto);

    void deleteById(long id);
}
