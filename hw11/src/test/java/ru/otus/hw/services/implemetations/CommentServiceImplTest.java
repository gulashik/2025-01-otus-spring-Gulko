package ru.otus.hw.services.implemetations;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.data.mongo.DataMongoTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.mongodb.core.MongoTemplate;
import ru.otus.hw.models.dto.BookDto;
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.objects.TestObjectsDb;
import ru.otus.hw.models.mappers.AuthorMapper;
import ru.otus.hw.models.mappers.BookMapper;
import ru.otus.hw.models.mappers.CommentMapper;
import ru.otus.hw.models.mappers.GenreMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.objects.TestObjects.getDbBooks;
import static ru.otus.hw.objects.TestObjects.getDbComments;

@DisplayName("Сервис на основе Mongo для работы с комментариями ")
@DataMongoTest
@Import(
    {
//        CommentServiceImpl.class, CommentMapper.class,
//        BookServiceImpl.class, BookMapper.class,
//        AuthorMapper.class,
//        GenreMapper.class
    }
)
class CommentServiceImplTest {

    @Autowired
    private MongoTemplate mongoTemplate;

//    @Autowired
//    private CommentServiceImpl service;

    private final List<BookDto> dbBookDtos = getDbBooks();

    private final List<CommentDto> dbCommentDtos = getDbComments();

    @BeforeEach
    void setUp() {
        TestObjectsDb.recreateTestObjects(mongoTemplate);
    }

//    @DisplayName("должен загружать комментарий по id")
//    @Test
//    void findById() {
//        var expectedComment = dbCommentDtos.get(0);
//        var actualComment = service.findById(expectedComment.getId()).block();
//
//        assertThat(actualComment)
//            .isEqualTo(expectedComment);
//    }
//
//    @DisplayName("должен загружать список всех комментариев по книге")
//    @Test
//    void findAllForBook() {
//        var id = dbBookDtos.get(0).getId();
//        List<CommentDto> excpectedCommentDtos = dbCommentDtos
//            .stream()
//            .filter(comment -> comment.getBookDto().getId().equals(id))
//            .toList();
//
//        var actualCommentDtos = service.findAllForBook(id).toStream().toList();
//
//        assertThat(actualCommentDtos).containsExactlyElementsOf(excpectedCommentDtos);
//    }
//
//    @DisplayName("должен сохранить комментарий")
//    @Test
//    void save() {
//        var expectedComment = dbCommentDtos.get(0);
//        expectedComment.setId("x");
//        expectedComment = service.save(expectedComment).block();
//
//        var actualComment = service.findById(expectedComment.getId()).block();
//
//        assertThat(actualComment).isEqualTo(expectedComment);
//    }
//
//    @DisplayName("должен обновить комментарий")
//    @Test
//    void update() {
//        var expectedComment = dbCommentDtos.get(0);
//        expectedComment.setText("-");
//
//        service.update(expectedComment.getId(), expectedComment.getText()).block();
//
//        var actualComment = service.findById(expectedComment.getId()).block();
//
//        assertThat(actualComment).isEqualTo(expectedComment);
//    }
//
//    @DisplayName("должен создать комментарий")
//    @Test
//    void create() {
//        var expectedComment = new CommentDto(null, "new", getDbBooks().get(0));
//
//        expectedComment = service.create(expectedComment.getBookDto().getId(), expectedComment.getText()).block();
//
//        var actualComment = service.findById(expectedComment.getId()).block();
//
//        assertThat(actualComment).isEqualTo(expectedComment);
//    }
//
//    @DisplayName("должен удалить комментарий")
//    @Test
//    void deleteById() {
//        var id = dbCommentDtos.get(0).getId();
//
//        service.deleteById(id);
//
//        var actualComment = service.findById(id).blockOptional();
//
//        assertThat(actualComment).isEmpty();
//    }
}