package ru.otus.hw.services;

import reactor.core.publisher.Flux;
import ru.otus.hw.models.dto.GenreDto;

import java.util.List;

public interface GenreService {
    Flux<GenreDto> findAll();
}
