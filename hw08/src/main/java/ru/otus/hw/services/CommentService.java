package ru.otus.hw.services;

import ru.otus.hw.models.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(String id);

    List<CommentDto> findAllForBook(String bookId);

    CommentDto save(CommentDto commentDto);

    CommentDto update(String id, String text);

    CommentDto create(String bookId, String text);

    void deleteById(String id);
}
