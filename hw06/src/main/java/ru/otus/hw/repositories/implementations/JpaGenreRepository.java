package ru.otus.hw.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.domains.Genre;
import ru.otus.hw.models.entities.GenreEntity;
import ru.otus.hw.models.mappers.GenreMapper;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JpaGenreRepository implements GenreRepository {

    @PersistenceContext
    private final EntityManager entityManager;

    private final GenreMapper genreMapper;

    @Override
    public List<Genre> findAll() {
        return entityManager
            .createQuery("select g from genres g", GenreEntity.class)
            .getResultList()
            .stream()
            .map(genreMapper::toDomain)
            .toList();
    }

    @Override
    public Optional<Genre> findById(long id) {
        var genreEntity = entityManager.find(GenreEntity.class, id);
        return Optional.ofNullable(genreMapper.toDomain(genreEntity));
    }

    @Override
    public Genre save(Genre genre) {
        var genreEntity = entityManager.merge(genreMapper.toEntity(genre));
        return genreMapper.toDomain(genreEntity);
    }
}
