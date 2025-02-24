package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.*;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import reactor.util.function.Tuple2;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Genre;
import ru.otus.hw.models.mappers.BookMapper;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;


@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1/")
public class BookController {
    private final BookRepository bookRepository;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookMapper bookMapper;

    private final CommentRepository commentRepository;

    // curl http://localhost:8080/api/v1/books
    @GetMapping("books")
    public Flux<BookDto> getBooks() {
        return bookRepository
            .findAll()
            .map(bookMapper::toDto);
    }

    // curl http://localhost:8080/api/v1/book/1
    @GetMapping("book/{id}")
    public Mono<BookDto> getBook(@PathVariable("id") String id) {
        return bookRepository
            .findById(id)
            .map(bookMapper::toDto);
    }

    // curl -X POST http://localhost:8080/api/v1/book \
    //-H "Content-Type: application/json" \
    //-d '{"id":null,"title":"BookTitle_x","authorDto":{"id":"1"},"genreDto":{"id":"1"}}'
    @PostMapping("/book")
    public Mono<BookDto> createBook(@RequestBody Mono<BookDto> monoBookDto) {
        return save(monoBookDto, null);
    }

    // curl -X PATCH http://localhost:8080/api/v1/book/1 \
    //-H "Content-Type: application/json" \
    //-d '{"id":1,"title":"BookTitle_x","authorDto":{"id":"2"},"genreDto":{"id":"2"}}'
    @PatchMapping("book/{id}")
    public Mono<BookDto> updateBook(@RequestBody Mono<BookDto> monoBookDto, @PathVariable("id") String id) {
        return save(monoBookDto, id);
    }

    // curl -X DELETE -s http://localhost:8080/api/v1/book/1
    @DeleteMapping("book/{id}")
    public Mono<Void> deleteBook(@PathVariable String id) {
        return commentRepository
            .deleteAllByBookId(id)
            .then(bookRepository.deleteById(id));
    }

    private Mono<BookDto> save(Mono<BookDto> monoBookDto, String id) {
        return monoBookDto
            .zipWhen(
                (BookDto bookDto) ->
                    authorRepository
                        .findById(bookDto.getAuthorDto().getId())
                        .switchIfEmpty(
                            Mono.error(new EntityNotFoundException(
                                "author with id = %s not found".formatted(bookDto.getAuthorDto().getId()))
                            )
                        )
            )
            .zipWhen(
                (Tuple2<BookDto, Author> tuple) ->
                    genreRepository
                        .findById(tuple.getT1().getGenreDto().getId())
                        .switchIfEmpty(Mono.error(new EntityNotFoundException("genres not found"))))
            .flatMap(
                (Tuple2<Tuple2<BookDto, Author>, Genre> tuple2) ->
                    bookRepository.save(
                        new Book(
                            id,
                            tuple2.getT1().getT1().getTitle(),
                            tuple2.getT1().getT2(),
                            tuple2.getT2()
                        )
                    )
            )
            .map(bookMapper::toDto);
    }
}
