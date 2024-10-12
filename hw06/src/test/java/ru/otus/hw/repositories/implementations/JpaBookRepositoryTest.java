package ru.otus.hw.repositories.implementations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.domains.Author;
import ru.otus.hw.models.domains.Book;
import ru.otus.hw.models.domains.Genre;
import ru.otus.hw.models.mappers.AuthorMapper;
import ru.otus.hw.models.mappers.BookMapper;
import ru.otus.hw.models.mappers.GenreMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.objects.TestObjects.*;

@DisplayName("Репозиторий на основе Jpa для работы с книгами ")
@DataJpaTest
@Import(
    {
        JpaBookRepository.class,  BookMapper.class,
        JpaGenreRepository.class, GenreMapper.class,
        JpaAuthorRepository.class, AuthorMapper.class
    }
)
class JpaBookRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaBookRepository bookRepository;

    private final List<Author> dbAuthors = getDbAuthors();

    private final List<Genre> dbGenres = getDbGenres();

    private final List<Book> dbBooks = getDbBooks();


    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getBooks")
    void shouldReturnCorrectBookById(Book expectedBook) {
        var actualBook = bookRepository.findById(expectedBook.getId());
        assertThat(actualBook).isPresent()
                .get()
                .isEqualTo(expectedBook);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void shouldReturnCorrectBooksList() {
        var actualBooks = bookRepository.findAll();
        var expectedBooks = dbBooks;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void shouldSaveNewBook() {
        var expectedBook = new Book(0, "BookTitle_10500", dbAuthors.get(0), dbGenres.get(0));
        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
            .matches(book -> book.getId() > 0)
            .usingRecursiveComparison()
            .ignoringExpectedNullFields()
            .ignoringFields("id")
            .isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен сохранять измененную книгу")
    @Test
    void shouldSaveUpdatedBook() {
        var expectedBook = new Book(1L, "BookTitle_10500", dbAuthors.get(2), dbGenres.get(2));

        assertThat(bookRepository.findById(expectedBook.getId()))
                .isPresent()
                .get()
                .isNotEqualTo(expectedBook);

        var returnedBook = bookRepository.save(expectedBook);
        assertThat(returnedBook).isNotNull()
                .matches(book -> book.getId() > 0)
                .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(bookRepository.findById(returnedBook.getId()))
                .isPresent()
                .get()
                .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void shouldDeleteBook() {
        long id = 1L;
        assertThat(bookRepository.findById(id)).isPresent();
        entityManager.clear();

        bookRepository.deleteById(id);

        assertThat(bookRepository.findById(id)).isEmpty();
    }

    public static List<Book> getBooks() {
        return getDbBooks();
    }
}