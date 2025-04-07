package ru.otus.hw.model.h2.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {
    private Long id;

    private String commentText;

    private Long bookId;
}
