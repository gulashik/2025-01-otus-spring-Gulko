package ru.otus.hw.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.models.Book;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.services.mappers.BookMapper;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@RequiredArgsConstructor
@Repository
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private final BookMapper bookMapper;

    @Override
    public Optional<ru.otus.hw.models.dto.Book> findById(long id) {
        Map<String, Object> hints = Map.of(
            FETCH.getKey(),
            entityManager.getEntityGraph("BookEntity-author-genre")
        );
        var bookEntity = entityManager.find(Book.class, id, hints);
        return Optional.ofNullable(bookMapper.toDomain(bookEntity));
    }

    @Override
    public List<ru.otus.hw.models.dto.Book> findAll() {
        var entityGraph = entityManager.getEntityGraph("BookEntity-author-genre");

        return entityManager
            .createQuery("select b from books b", Book.class)
            .setHint(FETCH.getKey(), entityGraph)
            .getResultList()
            .stream()
            .map(bookMapper::toDomain)
            .toList();
    }

    @Override
    public ru.otus.hw.models.dto.Book save(ru.otus.hw.models.dto.Book book) {
        var bookEntity = entityManager.merge(bookMapper.toEntity(book));
        return bookMapper.toDomain(bookEntity);
    }

    @Override
    public void deleteById(long id) {
        entityManager
            .createQuery("delete from books b where b.id = :id")
            .setParameter("id", id)
            .executeUpdate();
    }
}
