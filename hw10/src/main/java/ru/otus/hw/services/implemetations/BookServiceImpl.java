package ru.otus.hw.services.implemetations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.otus.hw.exceptions.NotFoundException;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Genre;
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

    @Transactional
    @Override
    public Optional<BookDto> findById(Long id) {
        return bookRepository
            .findById(id)
            .map(bookMapper::toDto);
    }

    @Transactional
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
    public BookDto insert(String title, Long authorId, Long genreId) {
        return save(0L, title, authorId, genreId);
    }

    @Transactional
    @Override
    public BookDto update(Long id, String title, Long authorId, Long genreId) {

        Book book = bookRepository
            .findById(id)
            .orElseThrow(() -> new NotFoundException("Book with id " + id + " not found"));

        Author author = authorRepository
            .findById(authorId)
            .orElseThrow(() -> new NotFoundException("Author with id " + authorId + " not found"));

        Genre genre = genreRepository
            .findById(genreId)
            .orElseThrow(() -> new NotFoundException("Genre with id " + genreId + " not found"));

        book.setTitle(title);
        book.setAuthor(author);
        book.setGenre(genre);

        return bookMapper.toDto(book);
    }

    @Transactional
    @Override
    public void deleteById(Long id) {
        bookRepository.deleteById(id);
    }


    private BookDto save(Long id, String title, Long authorId, Long genreId) {
        var author = authorRepository.findById(authorId)
                .orElseThrow(() -> new NotFoundException("Author with id %d not found".formatted(authorId)));
        var genre = genreRepository.findById(genreId)
                .orElseThrow(() -> new NotFoundException("Genre with id %d not found".formatted(genreId)));
        var book = new BookDto(id, title, authorMapper.toDto(author), genreMapper.toDto(genre));
        var savedBook = bookRepository.save(bookMapper.toEntity(book));
        return bookMapper.toDto(savedBook);
    }
}
