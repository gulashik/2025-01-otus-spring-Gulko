package ru.otus.hw.services;

import ru.otus.hw.models.dto.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Optional<Comment> findById(long id);

    List<Comment> findAllForBook(long bookId);

    Comment save(Comment comment);

    void deleteById(long id);
}
