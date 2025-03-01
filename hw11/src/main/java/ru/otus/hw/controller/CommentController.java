package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Comment;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.models.mappers.CommentMapper;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class CommentController {
    private final CommentRepository commentRepository;

    private final BookRepository bookRepository;

    private final CommentMapper commentMapper;

    @GetMapping(value = "/comments")
    public Flux<CommentDto> getComments() {
        return commentRepository
            .findAll()
            .map(commentMapper::toDto);
    }

    @GetMapping("/comment/{id}")
    public Mono<CommentDto> getComment(@PathVariable("id") String id) {
        return commentRepository
            .findById(id)
            .map(commentMapper::toDto);
    }

    @GetMapping(value = "/comment")
    public Flux<CommentDto> getCommentsByBook(@RequestParam(value = "book_id") String bookId) {
        return commentRepository
            .findAllByBookId(bookId)
            .map(commentMapper::toDto);
    }

    @PatchMapping("/comment/{id}")
    public Mono<CommentDto> updateComment(@RequestBody Mono<CommentDto> monoCommentDto, @PathVariable("id") String id) {
        return save(monoCommentDto, id);
    }

    @PostMapping("/comment")
    public Mono<CommentDto> createComment(@RequestBody Mono<CommentDto> monoCommentDto) {
        return save(monoCommentDto, null);
    }

    @DeleteMapping("/comment/{id}")
    public Mono<Void> deleteComment(@PathVariable String id) {
        return commentRepository.deleteById(id);
    }

    private Mono<CommentDto> save(Mono<CommentDto> monoCommentDto, String id) {
        return monoCommentDto
            .zipWhen(
                (CommentDto commentDto) ->
                    bookRepository
                        .findById(commentDto.getBookDto().getId())
                        .switchIfEmpty(
                            Mono.error(new EntityNotFoundException(
                                    "book with id = %s not found".formatted(commentDto.getBookDto().getId())
                                )
                            )
                        )
            )
            .flatMap(
                (Tuple2<CommentDto, Book> tuple) ->
                    commentRepository.save(
                        new Comment(
                            id,
                            tuple.getT1().getText(),
                            tuple.getT2()
                        )
                    )
            )
            .map(commentMapper::toDto);
    }
}
