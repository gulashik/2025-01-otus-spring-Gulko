package ru.otus.hw.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.domains.Author;
import ru.otus.hw.models.entities.AuthorEntity;
import ru.otus.hw.models.mappers.AuthorMapper;
import ru.otus.hw.repositories.AuthorRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaAuthorRepository implements AuthorRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private final AuthorMapper authorMapper;

    @Override
    public List<Author> findAll() {
        return entityManager
            .createQuery("select a from authors a", AuthorEntity.class)
            .getResultList()
            .stream()
            .map(authorMapper::toDomain)
            .toList();
    }

    @Override
    public Optional<Author> findById(long id) {
        var authorEntity = entityManager.find(AuthorEntity.class, id);
        return Optional.ofNullable(authorMapper.toDomain(authorEntity));
    }

    @Override
    public Author save(Author author) {
        var authorEntity = entityManager.merge(authorMapper.toEntity(author));
        return authorMapper.toDomain(authorEntity);
    }
}
