package ru.otus.hw.models.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.domains.Genre;
import ru.otus.hw.models.entities.GenreEntity;

@Component
public class GenreMapper {
    public Genre toDomain(GenreEntity entity) {
        if (entity == null) {
            return null;
        }
        return new Genre(entity.getId(), entity.getName());
    }

    public GenreEntity toEntity(Genre domain) {
        if (domain == null) {
            return null;
        }
        return new GenreEntity(domain.getId(), domain.getName());
    }
}
