package ru.otus.hw.services.implemetations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Comment;
import ru.otus.hw.repositories.BookRepository;
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

    private final BookRepository bookRepository;

    @Override
    public Optional<CommentDto> findById(String id) {
        return commentRepository
            .findById(id)
            .map(commentMapper::toDto);
    }

    @Override
    public List<CommentDto> findAllForBook(String bookId) {
        return commentRepository
            .findAllByBookId(bookId)
            .stream()
            .map(commentMapper::toDto)
            .toList();
    }

    @Override
    public CommentDto save(CommentDto commentDto) {
        var commentEntity = commentRepository.save(commentMapper.toEntity(commentDto));
        return commentMapper.toDto(commentEntity);
    }

    @Transactional
    @Override
    public CommentDto update(String id, String text) {
        Comment comment = commentRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Comment with id " + id + " not found"));

        comment.setText(text);

        return commentMapper.toDto(
            commentRepository.save(comment)
        );
    }

    @Transactional
    @Override
    public CommentDto create(String bookId, String text) {
        Book book = bookRepository.findById(bookId)
            .orElseThrow(() -> new EntityNotFoundException("Book with id " + bookId + " not found"));

        Comment comment = commentRepository.save(
            new Comment("0", text, book)
        );

        return commentMapper.toDto(comment);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        commentRepository.deleteById(id);
    }
}
