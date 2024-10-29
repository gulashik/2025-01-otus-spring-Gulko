package ru.otus.hw.services.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.models.Genre;

@Component
public class GenreMapper {
    public ru.otus.hw.models.dto.Genre toDomain(Genre entity) {
        if (entity == null) {
            return null;
        }

        return new ru.otus.hw.models.dto.Genre(entity.getId(), entity.getName());
    }

    public Genre toEntity(ru.otus.hw.models.dto.Genre domain) {
        if (domain == null) {
            return null;
        }

        return new Genre(domain.getId(), domain.getName());
    }
}
