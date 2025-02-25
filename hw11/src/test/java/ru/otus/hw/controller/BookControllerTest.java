package ru.otus.hw.controller;


import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import reactor.core.publisher.Mono;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.models.mappers.AuthorMapper;
import ru.otus.hw.models.mappers.BookMapper;
import ru.otus.hw.models.mappers.GenreMapper;
import ru.otus.hw.objects.TestObjectsDb;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.objects.TestObjects.*;

@DisplayName("Контроллер по Книгам")
@DataMongoTest
@Import(
    {
        BookController.class,
        BookMapper.class,
        AuthorMapper.class,
        GenreMapper.class
    }
)
class BookControllerTest {
    private final List<AuthorDto> dbAuthorDtos = getDbAuthors();

    private final List<GenreDto> dbGenreDtos = getDbGenres();

    private final List<BookDto> dbBookDtos = getDbBooks();

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private BookController bookController;

    @BeforeEach
    void setUp() {
        TestObjectsDb.recreateTestObjects(mongoTemplate);
    }

    @DisplayName("должен получать все книги")
    @Test
    void getBooks() {
        Iterable<BookDto> actualBooks = bookController.getBooks().toIterable();

        assertThat(actualBooks).containsExactlyElementsOf(dbBookDtos);
    }

    @DisplayName("должен получать книгу по id")
    @Test
    void getBook() {
        BookDto expectedBook = dbBookDtos.get(0);
        BookDto actualBook = bookController.getBook(expectedBook.getId()).block();

        assertThat(actualBook).isEqualTo(expectedBook);
    }

    @DisplayName("должен создавать книгу")
    @Test
    void createBook() {
        BookDto expectedBook = new BookDto(/*id*/null,/*title*/"TestTitle", /*author*/dbAuthorDtos.get(0),/*genre*/dbGenreDtos.get(0));
        BookDto actualBook = bookController.createBook(Mono.just(expectedBook)).block();

        assertThat(actualBook)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expectedBook);
    }

    @Test
    void updateBook() {
    }

    @Test
    void deleteBook() {
    }
}