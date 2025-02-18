package ru.otus.hw.repositories;

import org.springframework.data.mongodb.repository.ReactiveMongoRepository;
import ru.otus.hw.models.entity.Genre;

public interface GenreRepository extends ReactiveMongoRepository<Genre, String> {
}
