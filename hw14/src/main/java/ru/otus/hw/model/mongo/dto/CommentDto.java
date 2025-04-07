package ru.otus.hw.model.mongo.dto;

import lombok.AllArgsConstructor;
import lombok.Data;

@Data
@AllArgsConstructor
public class CommentDto {
    private String id;

    private String commentText;

    private String bookId;
}
