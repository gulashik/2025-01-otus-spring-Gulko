package ru.otus.hw.services.implemetations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.dto.Author;
import ru.otus.hw.models.dto.Book;
import ru.otus.hw.models.dto.Genre;
import ru.otus.hw.repositories.implementations.JpaAuthorRepository;
import ru.otus.hw.repositories.implementations.JpaBookRepository;
import ru.otus.hw.repositories.implementations.JpaGenreRepository;
import ru.otus.hw.services.mappers.AuthorMapper;
import ru.otus.hw.services.mappers.BookMapper;
import ru.otus.hw.services.mappers.GenreMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.objects.TestObjects.*;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import(
    {
        BookServiceImpl.class,
        JpaBookRepository.class,  BookMapper.class,
        JpaGenreRepository.class, GenreMapper.class,
        JpaAuthorRepository.class, AuthorMapper.class
    }
)
class BookServiceImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private BookServiceImpl service;

    private final List<Author> dbAuthors = getDbAuthors();

    private final List<Genre> dbGenres = getDbGenres();

    private final List<Book> dbBooks = getDbBooks();

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getBooks")
    void findById(Book expectedBook) {
        var actualBook = service.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
            .get()
            .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void findAll() {
        var actualBooks = service.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void insert() {
        var expectedBook = new Book(0, "BookTitle_10500", dbAuthors.get(0), dbGenres.get(0));
        var returnedBook = service.insert(
            expectedBook.getTitle(),
            expectedBook.getAuthor().getId(),
            expectedBook.getGenre().getId()
        );

        assertThat(returnedBook).isNotNull()
            .matches(book -> book.getId() > 0)
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .ignoringFields("id")
            .isEqualTo(expectedBook);

        assertThat(service.findById(returnedBook.getId()))
            .isPresent()
            .get()
            .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void update() {
        var expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2), dbGenres.get(2));

        assertThat(service.findById(expectedBook.getId()))
            .isPresent()
            .get()
            .isNotEqualTo(expectedBook);

        var returnedBook = service.update(
            expectedBook.getId(),
            expectedBook.getTitle(),
            expectedBook.getAuthor().getId(),
            expectedBook.getGenre().getId()
        );

        assertThat(returnedBook).isNotNull()
            .matches(book -> book.getId() > 0)
            .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(service.findById(returnedBook.getId()))
            .isPresent()
            .get()
            .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void deleteById() {
        long id = 1L;
        assertThat(service.findById(id)).isPresent();
        entityManager.clear();

        service.deleteById(id);

        assertThat(service.findById(id)).isEmpty();
    }

    public static List<Book> getBooks() {
        return getDbBooks();
    }
}