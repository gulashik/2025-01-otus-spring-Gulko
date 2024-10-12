package ru.otus.hw.repositories.implementations;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.context.annotation.Import;
import ru.otus.hw.models.domains.Book;
import ru.otus.hw.models.domains.Comment;
import ru.otus.hw.models.mappers.AuthorMapper;
import ru.otus.hw.models.mappers.BookMapper;
import ru.otus.hw.models.mappers.CommentMapper;
import ru.otus.hw.models.mappers.GenreMapper;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static ru.otus.hw.objects.TestObjects.*;

@DisplayName("Репозиторий на основе Jpa для работы с комментариями ")
@DataJpaTest
@Import(
    {
        JpaBookRepository.class,  BookMapper.class,
        JpaGenreRepository.class, GenreMapper.class,
        JpaAuthorRepository.class, AuthorMapper.class,
        JpaCommentRepository.class, CommentMapper.class
    }
)
class JpaCommentRepositoryTest {

    @Autowired
    private TestEntityManager entityManager;

    @Autowired
    private JpaCommentRepository commentRepository;

    private final List<Book> dbBooks = getDbBooks();

    private final List<Comment> dbComments = getDbComments();

    @DisplayName("должен загружать комментарий по id")
    @Test
    void shouldReturnCorrectCommentById() {
        var expectedComment = dbComments.get(0);
        var actualComment = commentRepository.findById(expectedComment.getId());

        assertThat(actualComment)
            .isPresent()
            .get()
            .isEqualTo(expectedComment);
    }

    @DisplayName("должен загружать список всех комментариев по книге")
    @Test
    void shouldReturnCorrectCommentsByBook() {
        long id = dbBooks.get(0).getId();
        List<Comment> excpectedComments = dbComments
            .stream()
            .filter(comment -> comment.getBook().getId() == id)
            .toList();

        List<Comment> actualComments = commentRepository.findAllForBook(id);

        assertThat(actualComments).containsExactlyElementsOf(excpectedComments);
    }

    @DisplayName("должен сохранить комментарий")
    @Test
    void shouldSaveComment() {
        var expectedComment = dbComments.get(0);
        expectedComment.setId(0L);
        expectedComment = commentRepository.save(expectedComment);

        entityManager.clear();
        var actualComment = commentRepository.findById(expectedComment.getId());

        assertThat(actualComment).isPresent().get().isEqualTo(expectedComment);
    }

    @DisplayName("должен удалить комментарий")
    @Test
    void shouldDeleteComment() {
        var id = dbComments.get(0).getId();

        commentRepository.deleteById(id);

        entityManager.clear();
        var actualComment = commentRepository.findById(id);

        assertThat(actualComment).isEmpty();
    }

}