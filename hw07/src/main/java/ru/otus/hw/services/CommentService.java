package ru.otus.hw.services;

import ru.otus.hw.models.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(long id);

    List<CommentDto> findAllForBook(long bookId);

    CommentDto save(CommentDto commentDto);

    void deleteById(long id);
}
