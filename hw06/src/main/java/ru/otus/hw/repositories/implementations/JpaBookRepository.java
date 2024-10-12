package ru.otus.hw.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.data.jpa.repository.EntityGraph;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.domains.Book;
import ru.otus.hw.models.entities.BookEntity;
import ru.otus.hw.models.mappers.BookMapper;
import ru.otus.hw.repositories.BookRepository;

import java.util.List;
import java.util.Optional;

import static org.springframework.data.jpa.repository.EntityGraph.EntityGraphType.FETCH;

@RequiredArgsConstructor
@Repository
public class JpaBookRepository implements BookRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private final BookMapper bookMapper;

    @EntityGraph("BookEntity-author-genre")
    @Override
    public Optional<Book> findById(long id) {
        var bookEntity = entityManager.find(BookEntity.class, id);
        return Optional.ofNullable(bookMapper.toDomain(bookEntity));
    }

    @Override
    public List<Book> findAll() {
        var entityGraph = entityManager.getEntityGraph("BookEntity-author-genre");

        return entityManager
            .createQuery("select b from books b", BookEntity.class)
            .setHint(FETCH.getKey(), entityGraph)
            .getResultList()
            .stream()
            .map(bookMapper::toDomain)
            .toList();
    }

    @Override
    public Book save(Book book) {
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
