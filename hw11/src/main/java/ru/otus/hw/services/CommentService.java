package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.dto.CommentDto;

import java.util.List;
import java.util.Optional;

public interface CommentService {
    Mono<CommentDto> findById(String id);

    Flux<CommentDto> findAllForBook(String bookId);

    Mono<CommentDto> save(CommentDto commentDto);

    Mono<CommentDto> update(String id, String text);

    Mono<CommentDto> create(String bookId, String text);

    void deleteById(String id);
}
