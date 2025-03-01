package ru.otus.hw.controller;

import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import reactor.core.publisher.Flux;
import ru.otus.hw.models.dto.AuthorDto;
import ru.otus.hw.models.mappers.AuthorMapper;
import ru.otus.hw.repositories.AuthorRepository;

@RequiredArgsConstructor
@RestController
@RequestMapping("/api/v1")
public class AuthorController {
    private final AuthorRepository repository;

    private final AuthorMapper mapper;

    // curl http://localhost:8080/api/v1/authors
    @GetMapping("/authors")
    public Flux<AuthorDto> getAuthors() {
        return repository
            .findAll()
            .map(mapper::toDto);
    }
}
