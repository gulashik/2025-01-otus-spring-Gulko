package ru.otus.hw.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.models.Comment;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.services.mappers.CommentMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@RequiredArgsConstructor
@Repository
public class JpaCommentRepository implements CommentRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private final CommentMapper commentMapper;

    @Override
    public Optional<ru.otus.hw.models.dto.Comment> findById(long id) {
        Map<String, Object> hints = Map.of(
            FETCH.getKey(),
            entityManager.getEntityGraph("CommentEntity-book")
        );
        var commentEntity = entityManager.find(Comment.class, id, hints);
        return Optional.ofNullable(commentMapper.toDomain(commentEntity));
    }

    @Override
    public List<ru.otus.hw.models.dto.Comment> findAllForBook(long bookId) {
        var entityGraph = entityManager.getEntityGraph("CommentEntity-book");
        return entityManager
            .createQuery("select c from comments c where c.book.id = :id", Comment.class)
            .setParameter("id", bookId)
            .setHint(FETCH.getKey(), entityGraph)
            .getResultList()
            .stream()
            .map(commentMapper::toDomain)
            .toList();
    }

    @Override
    public ru.otus.hw.models.dto.Comment save(ru.otus.hw.models.dto.Comment comment) {
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
