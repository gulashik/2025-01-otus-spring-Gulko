package ru.otus.hw.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.models.Book;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@RequiredArgsConstructor
@Repository
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public Optional<Book> findById(long id) {
        Map<String, Object> hints = Map.of(
            FETCH.getKey(),
            entityManager.getEntityGraph("BookEntity-author-genre")
        );
        return Optional.ofNullable(entityManager.find(Book.class, id, hints));
    }

    @Override
    public List<Book> findAll() {
        var entityGraph = entityManager.getEntityGraph("BookEntity-author-genre");

        return entityManager
            .createQuery("select b from books b", Book.class)
            .setHint(FETCH.getKey(), entityGraph)
            .getResultList()
            .stream()
            .toList();
    }

    @Override
    public Book save(Book book) {
        return entityManager.merge(book);
    }

    @Override
    public void deleteById(long id) {
        entityManager.remove(entityManager.find(Book.class, id));
    }
}
