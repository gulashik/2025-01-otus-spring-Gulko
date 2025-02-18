package ru.otus.hw.models.converters;

import org.springframework.stereotype.Component;
import ru.otus.hw.models.dto.GenreDto;

@Component
public class GenreConverter {
    public String genreToString(GenreDto genreDto) {
        return "Id: %s, Name: %s".formatted(genreDto.getId(), genreDto.getName());
    }
}
