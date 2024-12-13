package ru.otus.hw.models.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@AllArgsConstructor
@NoArgsConstructor
<<<<<<<< HEAD:hw10/src/main/java/ru/otus/hw/models/dto/BookPostDto.java
public class BookPostDto {
    private Long id;

========
public class BookCreateDto {
>>>>>>>> main:hw10/src/main/java/ru/otus/hw/models/dto/BookCreateDto.java
    private String title;

    private Long authorId;

    private Long genreId;
}