package ru.otus.hw.repositories.jpa;

import org.springframework.data.jpa.repository.JpaRepository;
import ru.otus.hw.model.targetdb.entity.Author;

public interface JpaAuthorRepository extends JpaRepository<Author, Long> {
}
