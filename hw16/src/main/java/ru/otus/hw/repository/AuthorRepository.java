package ru.otus.hw.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;
import org.springframework.data.rest.core.annotation.Description;
import ru.otus.hw.model.entity.Author;

@RepositoryRestResource(
    path = "authors",
    collectionResourceRel = "authors",
    collectionResourceDescription = @Description("Коллекция авторов"),
    itemResourceDescription = @Description("Автор")
)
public interface AuthorRepository extends JpaRepository<Author, Long> {
}