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
import ru.otus.hw.models.dto.CommentDto;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.models.mappers.AuthorMapper;
import ru.otus.hw.models.mappers.BookMapper;
import ru.otus.hw.models.mappers.CommentMapper;
import ru.otus.hw.models.mappers.GenreMapper;
import ru.otus.hw.objects.TestObjectsDb;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.objects.TestObjects.*;

@DisplayName("Контроллер по Комментариям")
@DataMongoTest
@Import(
    {
        CommentController.class,
        CommentMapper.class,
        BookMapper.class,
        AuthorMapper.class,
        GenreMapper.class
    }
)
class CommentControllerTest {
    private final List<AuthorDto> dbAuthorDtos = getDbAuthors();

    private final List<GenreDto> dbGenreDtos = getDbGenres();

    private final List<BookDto> dbBookDtos = getDbBooks();

    private final List<CommentDto> dbCommentDtos = getDbComments();

    @Autowired
    private MongoTemplate mongoTemplate;

    @Autowired
    private CommentController commentController;

    @BeforeEach
    void setUp() {
        TestObjectsDb.recreateTestObjects(mongoTemplate);
    }

    @DisplayName("должен получить все комментарии")
    @Test
    void getComments() {
        Iterable<CommentDto> actualComments = commentController.getComments().toIterable();

        // assert
        assertThat(actualComments).containsExactlyElementsOf(dbCommentDtos);
    }

    @DisplayName("должен получить комментарий")
    @Test
    void getComment() {
        CommentDto expectedComment = dbCommentDtos.get(0);
        CommentDto actualComment = commentController.getComment(expectedComment.getId()).block();

        //assert
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен получить все комментарии по книге")
    @Test
    void getCommentsByBook() {
        String bookId = dbBookDtos.get(0).getId();
        List<CommentDto> expectedComments = dbCommentDtos
            .stream()
            .filter(commentDto -> commentDto.getBookDto().getId().equals(bookId))
            .toList();

        List<CommentDto> actualComments = commentController
            .getCommentsByBook(bookId)
            .collectList()
            .block();

        // assert
        assertThat(actualComments).containsExactlyElementsOf(expectedComments);
    }

    @DisplayName("должен обновить комментарий в БД")
    @Test
    void updateComment() {
        CommentDto expectedComment = dbCommentDtos.get(0);
        expectedComment.setText("Test for Update");

        CommentDto actualComment = commentController.updateComment(
            Mono.just(expectedComment),
            expectedComment.getId()
        ).block();

        // assert
        assertThat(actualComment).isEqualTo(expectedComment);
    }

    @DisplayName("должен создать комментарий")
    @Test
    void createComment() {
        CommentDto expectedComment = dbCommentDtos.get(0);
        expectedComment.setText("Test for Create");
        expectedComment.setId(null);

        CommentDto actualComment = commentController
            .createComment(Mono.just(expectedComment))
            .block();

        // assert
        assertThat(actualComment)
            .usingRecursiveComparison()
            .ignoringFields("id")
            .isEqualTo(expectedComment);
    }

    @DisplayName("должен удалять комментарий")
    @Test
    void deleteComment() {
        String commentId = dbCommentDtos.get(0).getId();

        commentController.deleteComment(commentId).block();
        Optional<CommentDto> actualComment = commentController.getComment(commentId).blockOptional();

        // assert
        assertThat(actualComment).isEmpty();
    }
}