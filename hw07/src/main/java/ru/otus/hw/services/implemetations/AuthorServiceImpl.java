package ru.otus.hw.services.implemetations;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.otus.hw.models.dto.Author;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.services.AuthorService;
import ru.otus.hw.services.mappers.AuthorMapper;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AuthorServiceImpl implements AuthorService {

    private final AuthorRepository authorRepository;

    private final AuthorMapper authorMapper;

    @Override
    public List<Author> findAll() {
        return authorRepository
            .findAll()
            .stream()
            .map(authorMapper::toDomain)
            .toList();
    }
}
