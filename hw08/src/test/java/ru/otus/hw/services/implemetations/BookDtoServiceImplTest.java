package ru.otus.hw.services.implemetations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.services.mappers.AuthorMapper;
import ru.otus.hw.services.mappers.BookMapper;
import ru.otus.hw.services.mappers.GenreMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.objects.TestObjects.*;

@DisplayName("Репозиторий на основе Mongo для работы с книгами ")
@DataMongoTest
@Import(
    {
        BookServiceImpl.class, BookMapper.class,
        GenreMapper.class,
        AuthorMapper.class
    }
)
class BookDtoServiceImplTest {

    @Autowired
    private MongoTemplate entityManager;

    @Autowired
    private BookServiceImpl service;

    private final List<AuthorDto> dbAuthorDtos = getDbAuthors();

    private final List<GenreDto> dbGenreDtos = getDbGenres();

    private final List<BookDto> dbBookDtos = getDbBooks();

    @DisplayName("должен загружать книгу по id")
    @ParameterizedTest
    @MethodSource("getBooks")
    void findById(BookDto expectedBookDto) {
        var actualBook = service.findById(expectedBookDto.getId());
        assertThat(actualBook).isPresent()
            .get()
            .isEqualTo(expectedBookDto);
    }

    @DisplayName("должен загружать список всех книг")
    @Test
    void findAll() {
        var actualBooks = service.findAll();
        var expectedBooks = dbBookDtos;

        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
        actualBooks.forEach(System.out::println);
    }

    @DisplayName("должен сохранять новую книгу")
    @Test
    void insert() {
        var expectedBook = new BookDto(null, "BookTitle_10500", dbAuthorDtos.get(0), dbGenreDtos.get(0));
        var returnedBook = service.insert(
            expectedBook.getTitle(),
            expectedBook.getAuthorDto().getId(),
            expectedBook.getGenreDto().getId()
        );

        assertThat(returnedBook).isNotNull()
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
        var expectedBook = new BookDto("1", "BookTitle_10500", dbAuthorDtos.get(2), dbGenreDtos.get(2));

        assertThat(service.findById(expectedBook.getId()))
            .isPresent()
            .get()
            .isNotEqualTo(expectedBook);

        var returnedBook = service.update(
            expectedBook.getId(),
            expectedBook.getTitle(),
            expectedBook.getAuthorDto().getId(),
            expectedBook.getGenreDto().getId()
        );

        assertThat(returnedBook).isNotNull()
            .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);

        assertThat(service.findById(returnedBook.getId()))
            .isPresent()
            .get()
            .isEqualTo(returnedBook);
    }

    @DisplayName("должен удалять книгу по id ")
    @Test
    void deleteById() {
        var id = "1";
        assertThat(service.findById(id)).isPresent();

        service.deleteById(id);

        assertThat(service.findById(id)).isEmpty();
    }

    public static List<BookDto> getBooks() {
        return getDbBooks();
    }
}