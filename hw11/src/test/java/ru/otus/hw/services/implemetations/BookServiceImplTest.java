package ru.otus.hw.services.implemetations;

import org.junit.jupiter.api.BeforeEach;
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
import ru.otus.hw.objects.TestObjectsDb;
import ru.otus.hw.models.mappers.AuthorMapper;
import ru.otus.hw.models.mappers.BookMapper;
import ru.otus.hw.models.mappers.CommentMapper;
import ru.otus.hw.models.mappers.GenreMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.objects.TestObjects.*;

@DisplayName("Репозиторий на основе Mongo для работы с книгами ")
@DataMongoTest
@Import(
    {
//        BookServiceImpl.class, BookMapper.class,
//        CommentServiceImpl.class, CommentMapper.class,
//        GenreMapper.class,
//        AuthorMapper.class
    }
)
class BookServiceImplTest {

//    @Autowired
//    private MongoTemplate mongoTemplate;
//
//
//    @Autowired
//    private BookServiceImpl bookService;
//
//    @Autowired
//    private CommentServiceImpl commentService;
//
//    private final List<AuthorDto> dbAuthorDtos = getDbAuthors();
//
//    private final List<GenreDto> dbGenreDtos = getDbGenres();
//
//    private final List<BookDto> dbBookDtos = getDbBooks();
//    @Autowired
//    private BookMapper bookMapper;
//
//    @BeforeEach
//    void setUp() {
//        TestObjectsDb.recreateTestObjects(mongoTemplate);
//    }
//
//    @DisplayName("должен загружать книгу по id")
//    @ParameterizedTest
//    @MethodSource("getBooks")
//    void findById(BookDto expectedBookDto) {
//        mongoTemplate.save(bookMapper.toEntity(expectedBookDto));
//        var actualBook = bookService.findById(expectedBookDto.getId()).blockOptional();
//        assertThat(actualBook).isPresent()
//            .get()
//            .isEqualTo(expectedBookDto);
//    }
//
//    @DisplayName("должен загружать список всех книг")
//    @Test
//    void findAll() {
//        var actualBooks = bookService.findAll().toIterable();
//        var expectedBooks = dbBookDtos;
//
//        assertThat(actualBooks).containsExactlyElementsOf(expectedBooks);
//        actualBooks.forEach(System.out::println);
//    }
//
//    @DisplayName("должен сохранять новую книгу")
//    @Test
//    void insert() {
//        var expectedBook = new BookDto(null, "BookTitle_10500", dbAuthorDtos.get(0), dbGenreDtos.get(0));
//        var returnedBook = bookService.insert(
//            expectedBook.getTitle(),
//            expectedBook.getAuthorDto().getId(),
//            expectedBook.getGenreDto().getId()
//        ).block();
//
//        assertThat(returnedBook).isNotNull()
//            .usingRecursiveComparison()
//            .ignoringExpectedNullFields()
//            .ignoringFields("id")
//            .isEqualTo(expectedBook);
//
//        assertThat(bookService.findById(returnedBook.getId()).block())
//            .isEqualTo(returnedBook);
//    }
//
//    @DisplayName("должен сохранять измененную книгу")
//    @Test
//    void update() {
//        var expectedBook = new BookDto("1", "BookTitle_10500", dbAuthorDtos.get(2), dbGenreDtos.get(2));
//
//        assertThat(bookService.findById(expectedBook.getId()).block())
//            .isNotEqualTo(expectedBook);
//
//        var returnedBook = bookService.update(
//            expectedBook.getId(),
//            expectedBook.getTitle(),
//            expectedBook.getAuthorDto().getId(),
//            expectedBook.getGenreDto().getId()
//        ).block();
//
//        assertThat(returnedBook).isNotNull()
//            .usingRecursiveComparison().ignoringExpectedNullFields().isEqualTo(expectedBook);
//
//        assertThat(bookService.findById(returnedBook.getId()).block())
//            .isEqualTo(returnedBook);
//    }
//
//    @DisplayName("должен удалять книгу по id ")
//    @Test
//    void deleteById() {
//        var id = "1";
//        assertThat(bookService.findById(id).block()).isNotNull();
//
//        bookService.deleteById(id);
//
//        assertThat(bookService.findById(id).blockOptional()).isEmpty();
//
//        assertThat(commentService.findAllForBook(id).toIterable()).isEmpty();
//    }

    public static List<BookDto> getBooks() {
        return getDbBooks();
    }
}