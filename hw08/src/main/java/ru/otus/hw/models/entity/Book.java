package ru.otus.hw.models.entity;

import lombok.*;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.MongoId;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Document(collection = "books")
public class Book {
    @MongoId
    private String id;

    private String title;

    @DBRef
    private Author author;

    @DBRef
    private Genre genre;
}
