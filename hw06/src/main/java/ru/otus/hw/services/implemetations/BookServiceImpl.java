package ru.otus.hw.services.implemetations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.EntityNotFoundException;
import ru.otus.hw.models.dto.Book;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.services.BookService;
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

    private final BookMapper bookMapper;

    private final AuthorMapper authorMapper;

    private final GenreMapper genreMapper;

    @Override
    public Optional<Book> findById(long id) {
        return bookRepository
            .findById(id)
            .map(bookMapper::toDomain);
    }

    @Override
    public List<Book> findAll() {
        return bookRepository
            .findAll()
            .stream()
            .map(bookMapper::toDomain)
            .toList();
    }

    @Transactional
    @Override
    public Book insert(String title, long authorId, long genreId) {
        return save(0, title, authorId, genreId);
    }

    @Transactional
    @Override
    public Book update(long id, String title, long authorId, long genreId) {
        if (findById(id).isEmpty()) {
            throw new EntityNotFoundException("Book with id " + id + " not found");
        }

        return save(id, title, authorId, genreId);
    }

    @Transactional
    @Override
    public void deleteById(long id) {
        bookRepository.deleteById(id);
    }


    private Book save(long id, String title, long authorId, long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new EntityNotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new EntityNotFoundException("Genre with id %d not found".formatted(genreId)));
        var book = new Book(id, title, authorMapper.toDomain(author), genreMapper.toDomain(genre));
        var savedBook = bookRepository.save(bookMapper.toEntity(book));
        return bookMapper.toDomain(savedBook);
    }
}
