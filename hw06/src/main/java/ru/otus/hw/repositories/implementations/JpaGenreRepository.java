package ru.otus.hw.repositories.implementations;

import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.models.Genre;
import ru.otus.hw.services.mappers.GenreMapper;
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
    public List<ru.otus.hw.models.dto.Genre> findAll() {
        return entityManager
            .createQuery("select g from genres g", Genre.class)
            .getResultList()
            .stream()
            .map(genreMapper::toDomain)
            .toList();
    }

    @Override
    public Optional<ru.otus.hw.models.dto.Genre> findById(long id) {
        var genreEntity = entityManager.find(Genre.class, id);
        return Optional.ofNullable(genreMapper.toDomain(genreEntity));
    }

    @Override
    public ru.otus.hw.models.dto.Genre save(ru.otus.hw.models.dto.Genre genre) {
        var genreEntity = entityManager.merge(genreMapper.toEntity(genre));
        return genreMapper.toDomain(genreEntity);
    }
}
