package ru.otus.hw.models.entities;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.NamedEntityGraph;
import jakarta.persistence.NamedAttributeNode;
import jakarta.persistence.OneToOne;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.Id;
import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;


@Data
@NoArgsConstructor
@AllArgsConstructor
@Entity(name = "books")
@NamedEntityGraph(
    name = "BookEntity-author-genre",
    attributeNodes = { @NamedAttributeNode("author"), @NamedAttributeNode("genre") }
)
public class BookEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private long id;

    @Column(name = "title")
    private String title;

    @OneToOne
    @JoinColumn(name = "author_id")
    private AuthorEntity author;

    @OneToOne
    @JoinColumn(name = "genre_id")
    private GenreEntity genre;
}
