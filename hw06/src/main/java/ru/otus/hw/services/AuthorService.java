package ru.otus.hw.services;

import ru.otus.hw.models.domains.Author;

import java.util.List;

public interface AuthorService {
    List<Author> findAll();
}
