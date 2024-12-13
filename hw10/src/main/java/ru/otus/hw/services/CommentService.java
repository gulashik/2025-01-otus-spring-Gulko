package ru.otus.hw.services;

import ru.otus.hw.models.dto.CommentDto;

import java.util.List;

public interface CommentService {
    CommentDto findById(long id);

    List<CommentDto> findAllForBook(long bookId);

    CommentDto save(CommentDto commentDto);

    CommentDto update(long id, String text);

    CommentDto create(Long bookId, String text);

    void deleteById(long id);
}
