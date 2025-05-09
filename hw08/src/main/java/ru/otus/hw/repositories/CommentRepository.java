package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.MongoRepository;
import ru.otus.hw.models.entity.Comment;

import java.util.List;

public interface CommentRepository extends MongoRepository<Comment, String> {
   List<Comment> findAllByBookId(String bookId);
}