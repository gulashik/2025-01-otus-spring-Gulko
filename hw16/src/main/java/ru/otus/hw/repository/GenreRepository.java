package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.Description;
import ru.otus.hw.model.entity.Genre;

@RepositoryRestResource(
    path = "genres",
    collectionResourceRel = "genres",
    collectionResourceDescription = @Description("Коллекция жанров"),
    itemResourceDescription = @Description("Жанр")
)
public interface GenreRepository extends JpaRepository<Genre, Long> {
}