package ru.otus.hw.models.models;

import jakarta.persistence.*;
import lombok.*;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "comments")
@NamedEntityGraph(
    name = "CommentEntity-book",
    attributeNodes = {@NamedAttributeNode(value = "book", subgraph = "bookAttr")},
    subgraphs = {
        @NamedSubgraph(
            name = "bookAttr",
            attributeNodes =  { @NamedAttributeNode("author"), @NamedAttributeNode("genre") })
    }
)
public class Comment {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "text")
    private String text;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "book_id")
    private Book book;
}


