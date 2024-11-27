package ru.otus.hw.services.implemetations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
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

import java.util.List;
import java.util.Optional;

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
    public Optional<BookDto> findById(String id) {
        return bookRepository
            .findById(id)
            .map(bookMapper::toDto);
    }

    @Override
    public List<BookDto> findAll() {
        return bookRepository
            .findAll()
            .stream()
            .map(bookMapper::toDto)
            .toList();
    }

    @Transactional
    @Override
    public BookDto insert(String title, String authorId, String genreId) {
        return save(null, title, authorId, genreId);
    }

    @Transactional
    @Override
    public BookDto update(String id, String title, String authorId, String genreId) {

        Book book = bookRepository
            .findById(id)
            .orElseThrow(() -> new EntityNotFoundException("Book with id " + id + " not found"));

        Author author = authorRepository
            .findById(authorId)
            .orElseThrow(() -> new EntityNotFoundException("Author with id " + authorId + " not found"));

        Genre genre = genreRepository
            .findById(genreId)
            .orElseThrow(() -> new EntityNotFoundException("Genre with id " + genreId + " not found"));

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);

        Book saved = bookRepository.save(book);
        return bookMapper.toDto(saved);
    }

    @Transactional
    @Override
    public void deleteById(String id) {
        bookRepository.deleteById(id);
        commentService.findAllForBook(id)
            .stream()
            .map(CommentDto::getId)
            .forEach(commentService::deleteById);
    }


    private BookDto save(String id, String title, String authorId, String genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %s not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %s not found".formatted(genreId)));
        var book = new BookDto(id, title, authorMapper.toDto(author), genreMapper.toDto(genre));
        var savedBook = bookRepository.save(bookMapper.toEntity(book));
        return bookMapper.toDto(savedBook);
    }
}
