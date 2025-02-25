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
import ru.otus.hw.models.mappers.AuthorMapper;
import ru.otus.hw.models.mappers.BookMapper;
import ru.otus.hw.models.mappers.GenreMapper;
import ru.otus.hw.objects.TestObjectsDb;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.objects.TestObjects.*;

@DisplayName("Контроллер по Авторам")
@DataMongoTest
@Import(
    {
        AuthorController.class,
        BookMapper.class,
        AuthorMapper.class,
        GenreMapper.class
    }
)
class AuthorControllerTest {
    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private AuthorController authorController;

    @Autowired
    private AuthorRepository authorRepository;

    @Autowired
    private AuthorMapper authorMapper;

    @Autowired
    private BookMapper bookMapper;

    private final List<AuthorDto> dbAuthorDtos = getDbAuthors();

    private final List<GenreDto> dbGenreDtos = getDbGenres();

    private final List<BookDto> dbBookDtos = getDbBooks();


    @BeforeEach
    void setUp() {
        TestObjectsDb.recreateTestObjects(mongoTemplate);
    }

    @DisplayName("должен получать всех авторов")
    @Test
    void getAuthors() {
        Iterable<AuthorDto> actualAuthors = authorController.getAuthors().toIterable();
        Iterable<AuthorDto> expectedAuthors = dbAuthorDtos;

        assertThat(actualAuthors).containsExactlyElementsOf(expectedAuthors);
    }
}