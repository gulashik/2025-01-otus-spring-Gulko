package ru.otus.hw.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.models.mappers.GenreMapper;
import ru.otus.hw.objects.TestObjectsDb;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.objects.TestObjects.*;

@DisplayName("Контроллер по Жанрам")
@DataMongoTest
@Import(
    {
        GenreController.class,
        GenreMapper.class
    }
)
class GenreControllerTest {
    private final List<AuthorDto> dbAuthorDtos = getDbAuthors();

    private final List<GenreDto> dbGenreDtos = getDbGenres();

    private final List<BookDto> dbBookDtos = getDbBooks();

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private GenreController genreController;

    @BeforeEach
    void setUp() {
        TestObjectsDb.recreateTestObjects(mongoTemplate);
    }

    @DisplayName("должен получать все жанры")
    @Test
    void getGenres() {
        Iterable<GenreDto> actualGenders = genreController.getGenres().toIterable();

        // assert
        assertThat(actualGenders).containsExactlyElementsOf(dbGenreDtos);
    }
}