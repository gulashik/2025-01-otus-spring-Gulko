package ru.otus.hw.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.models.Comment;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;


@RequiredArgsConstructor
@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Comment> findById(long id) {
        return Optional.ofNullable(entityManager.find(Comment.class, id));
    }

    @Override
    public List<Comment> findAllForBook(long bookId) {
        return entityManager
            .createQuery("select c from comments c where c.book.id = :id", Comment.class)
            .setParameter("id", bookId)
            .getResultList()
            .stream()
            .toList();
    }

    @Override
    public Comment save(Comment comment) {
        return entityManager.merge(comment);
    }

    @Override
    public void deleteById(long id) {
        entityManager
            .createQuery("delete from comments where id = :id")
            .setParameter("id", id)
            .executeUpdate();
    }
}
