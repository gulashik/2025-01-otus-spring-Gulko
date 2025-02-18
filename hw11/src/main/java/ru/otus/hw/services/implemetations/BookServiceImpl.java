package ru.otus.hw.services.implemetations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import reactor.core.Disposable;
import reactor.core.publisher.Flux;
import reactor.core.publisher.Mono;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.BookService;
import ru.otus.hw.services.CommentService;
import ru.otus.hw.services.mappers.AuthorMapper;
import ru.otus.hw.services.mappers.BookMapper;
import ru.otus.hw.services.mappers.GenreMapper;

@RequiredArgsConstructor
@Service
public class BookServiceImpl implements BookService {
    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final BookRepository bookRepository;

    private final CommentService commentService;

    private final BookMapper bookMapper;

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    @Override
    public Mono<BookDto> findById(String id) {
        return bookRepository
            .findById(id)
            .map(bookMapper::toDto);
    }

    @Override
    public Flux<BookDto> findAll() {
        return bookRepository
            .findAll()
            .map(bookMapper::toDto);
    }

    @Transactional
    @Override
    public Mono<BookDto> insert(String title, String authorId, String genreId) {
        return save(null, title, authorId, genreId);
    }

    @Transactional
    @Override
    public Mono<BookDto> update(String id, String title, String authorId, String genreId) {

        Book book = bookRepository
            .findById(id)
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Book with id " + id + " not found")))
            .block();

        Author author = authorRepository
            .findById(authorId)
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id " + authorId + " not found")))
            .block();

        Genre genre = genreRepository
            .findById(genreId)
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Genre with id " + genreId + " not found")))
            .block();

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);

        return bookRepository
            .save(book)
            .map(bookMapper::toDto);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        commentService.findAllForBook(id)
            .map(CommentDto::getId)
            .toIterable()
            .forEach(commentService::deleteById);

        bookRepository
            .deleteById(id)
            .block();
    }

    private Mono<BookDto> save(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Author with id %s not found".formatted(authorId))));
        var genre = genreRepository.findById(genreId)
            .switchIfEmpty(Mono.error(new EntityNotFoundException("Genre with id %s not found".formatted(genreId))));
        var book = new BookDto(id, title, authorMapper.toDto(author.block()), genreMapper.toDto(genre.block()));

        return bookRepository
            .save(bookMapper.toEntity(book))
            .map(bookMapper::toDto);
    }
}
