package ru.otus.hw.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.models.Author;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaAuthorRepository implements AuthorRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    @Override
    public List<Author> findAll() {
        return entityManager
            .createQuery("select a from authors a", Author.class)
            .getResultList()
            .stream()
            .toList();
    }

    @Override
    public Optional<Author> findById(long id) {
        return Optional.ofNullable(entityManager.find(Author.class, id));
    }

    @Override
    public Author save(Author author) {
        return entityManager.merge(author);
    }
}
