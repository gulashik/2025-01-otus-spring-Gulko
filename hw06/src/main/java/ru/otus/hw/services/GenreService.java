package ru.otus.hw.services;

import ru.otus.hw.models.dto.Genre;

import java.util.List;

public interface GenreService {
    List<Genre> findAll();
}
