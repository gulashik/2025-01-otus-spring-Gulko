package ru.otus.hw.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.domains.Comment;
import ru.otus.hw.models.entities.CommentEntity;
import ru.otus.hw.models.mappers.CommentMapper;
import ru.otus.hw.repositories.CommentRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@RequiredArgsConstructor
@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private final CommentMapper commentMapper;

    @EntityGraph("CommentEntity-book")
    @Override
    public Optional<Comment> findById(long id) {
        var commentEntity = entityManager.find(CommentEntity.class, id);
        return Optional.ofNullable(commentMapper.toDomain(commentEntity));
    }

    @Override
    public List<Comment> findAllForBook(long bookId) {
        var entityGraph = entityManager.getEntityGraph("CommentEntity-book");
        return entityManager
            .createQuery("select c from comments c where c.book.id = :id", CommentEntity.class)
            .setParameter("id", bookId)
            .setHint(FETCH.getKey(), entityGraph)
            .getResultList()
            .stream()
            .map(commentMapper::toDomain)
            .toList();
    }

    @Override
    public Comment save(Comment comment) {
        var commentEntity = entityManager.merge(commentMapper.toEntity(comment));
        return commentMapper.toDomain(commentEntity);
    }

    @Override
    public void deleteById(long id) {
        entityManager
            .createQuery("delete from comments where id = :id")
            .setParameter("id", id)
            .executeUpdate();
    }
}
