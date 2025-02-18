package ru.otus.hw.services.implemetations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.entity.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.mappers.CommentMapper;

@RequiredArgsConstructor
@Service
public class CommentServiceImpl implements CommentService {

    private final CommentRepository commentRepository;

    private final CommentMapper commentMapper;

    private final BookRepository bookRepository;

    @Override
    public Mono<CommentDto> findById(String id) {
        return commentRepository
            .findById(id)
            .map(commentMapper::toDto);
    }

    @Override
    public Flux<CommentDto> findAllForBook(String bookId) {
        return commentRepository
            .findAllByBookId(bookId)
            .map(commentMapper::toDto);
    }

    @Override
    public Mono<CommentDto> save(CommentDto commentDto) {
        return commentRepository
            .save(commentMapper.toEntity(commentDto))
            .map(commentMapper::toDto);
    }

    @Transactional
    @Override
    public Mono<CommentDto> update(String id, String text) {
        var comment = commentRepository
            .findById(id)
            .doOnNext(curComment -> curComment.setText(text))
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Comment with id " + id + " not found")));

        return comment
            .flatMap(commentRepository::save)
            .map(commentMapper::toDto);
    }

    @Transactional
    @Override
    public Mono<CommentDto> create(String bookId, String text) {
        var book = bookRepository.findById(bookId)
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id " + bookId + " not found")));


        return book
            .flatMap( curBook -> commentRepository.save( new Comment("0", text, curBook) ) )
            .map(commentMapper::toDto);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
         commentRepository.deleteById(id).block();
    }
}
