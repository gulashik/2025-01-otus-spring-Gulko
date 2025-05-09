package ru.otus.hw.repositories;

import ru.otus.hw.models.models.Comment;

import java.util.List;
import java.util.Optional;

public interface CommentRepository {
    Optional<Comment> findById(long id);

    List<Comment> findAllForBook(long bookId);

    Comment save(Comment comment);

    void deleteById(long id);
}
