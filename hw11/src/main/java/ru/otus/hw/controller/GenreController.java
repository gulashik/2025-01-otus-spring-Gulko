package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.dto.GenreDto;
import ru.otus.hw.repositories.GenreRepository;
import ru.otus.hw.models.mappers.GenreMapper;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class GenreController {
    private final GenreRepository repository;

    private final GenreMapper mapper;

    // curl http://localhost:8080/api/v1/genres
    @GetMapping("/genres")
    public Flux<GenreDto> getGenres() {
        return repository.findAll().map(mapper::toDto);
    }
}
