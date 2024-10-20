package ru.otus.hw.services.implemetations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.dto.Book;
import ru.otus.hw.models.dto.Comment;
import ru.otus.hw.services.mappers.AuthorMapper;
import ru.otus.hw.services.mappers.BookMapper;
import ru.otus.hw.services.mappers.CommentMapper;
import ru.otus.hw.services.mappers.GenreMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.objects.TestObjects.getDbBooks;
import static ru.otus.hw.objects.TestObjects.getDbComments;

@DisplayName("Сервис на основе Jpa для работы с комментариями ")
@DataJpaTest
@Import(
    {
        CommentServiceImpl.class, CommentMapper.class,
        BookMapper.class,
        AuthorMapper.class,
        GenreMapper.class
    }
)
class CommentServiceImplTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private CommentServiceImpl service;

    private final List<Book> dbBooks = getDbBooks();

    private final List<Comment> dbComments = getDbComments();

    @DisplayName("должен загружать комментарий по id")
    @Test
    void findById() {
        var expectedComment = dbComments.get(0);
        var actualComment = service.findById(expectedComment.getId());

        assertThat(actualComment)
            .isPresent()
            .get()
            .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список всех комментариев по книге")
    @Test
    void findAllForBook() {
        long id = dbBooks.get(0).getId();
        List<Comment> excpectedComments = dbComments
            .stream()
            .filter(comment -> comment.getBook().getId() == id)
            .toList();

        List<Comment> actualComments = service.findAllForBook(id);

        assertThat(actualComments).containsExactlyElementsOf(excpectedComments);
    }

    @DisplayName("должен сохранить комментарий")
    @Test
    void save() {
        var expectedComment = dbComments.get(0);
        expectedComment.setId(0L);
        expectedComment = service.save(expectedComment);

        entityManager.clear();
        var actualComment = service.findById(expectedComment.getId());

        assertThat(actualComment).isPresent().get().isEqualTo(expectedComment);
    }

    @DisplayName("должен удалить комментарий")
    @Test
    void deleteById() {
        var id = dbComments.get(0).getId();

        service.deleteById(id);

        entityManager.flush();

        var actualComment = service.findById(id);

        assertThat(actualComment).isEmpty();
    }
}