package ru.otus.hw.services.implemetations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.models.dto.Comment;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.mappers.CommentMapper;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    @Override
    public Optional<Comment> findById(long id) {
        return commentRepository
            .findById(id)
            .map(commentMapper::toDomain);
    }

    @Override
    public List<Comment> findAllForBook(long bookId) {
        return commentRepository
//            .findAllForBook(bookId)
            .findAllByBookId(bookId)
            .stream()
            .map(commentMapper::toDomain)
            .toList();
    }

    @Override
    public Comment save(Comment comment) {
        ru.otus.hw.models.models.Comment commentEntity = commentRepository.save(commentMapper.toEntity(comment));
        return commentMapper.toDomain(commentEntity);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        commentRepository.deleteById(id);
    }
}
