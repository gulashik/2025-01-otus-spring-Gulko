package ru.otus.hw.services.mappers;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.models.entity.Genre;

@Component
public class GenreMapper {
    public GenreDto toDto(Genre entity) {
        if (entity == null) {
            return null;
        }
        return new GenreDto(entity.getId(), entity.getName());
    }

    public Genre toEntity(GenreDto dto) {
        return new Genre(dto.getId(), dto.getName());
    }
}
