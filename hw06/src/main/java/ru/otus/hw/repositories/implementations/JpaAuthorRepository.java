package ru.otus.hw.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.models.Author;
import ru.otus.hw.services.mappers.AuthorMapper;
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
    public List<ru.otus.hw.models.dto.Author> findAll() {
        return entityManager
            .createQuery("select a from authors a", Author.class)
            .getResultList()
            .stream()
            .map(authorMapper::toDomain)
            .toList();
    }

    @Override
    public Optional<ru.otus.hw.models.dto.Author> findById(long id) {
        var authorEntity = entityManager.find(Author.class, id);
        return Optional.ofNullable(authorMapper.toDomain(authorEntity));
    }

    @Override
    public ru.otus.hw.models.dto.Author save(ru.otus.hw.models.dto.Author author) {
        var authorEntity = entityManager.merge(authorMapper.toEntity(author));
        return authorMapper.toDomain(authorEntity);
    }
}
