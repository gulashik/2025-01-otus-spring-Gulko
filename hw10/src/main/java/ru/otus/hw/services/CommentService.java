package ru.otus.hw.services;

import ru.otus.hw.models.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<CommentDto> findById(Long id);

    List<CommentDto> findAllForBook(Long bookId);

    CommentDto save(CommentDto commentDto);

    CommentDto update(Long id, String text);

    CommentDto create(Long bookId, String text);

    void deleteById(Long id);
}
