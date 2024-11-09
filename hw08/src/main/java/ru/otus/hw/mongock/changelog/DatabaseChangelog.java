package ru.otus.hw.mongock.changelog;

import com.github.cloudyrock.mongock.ChangeLog;
import com.github.cloudyrock.mongock.ChangeSet;
import com.mongodb.client.MongoDatabase;
import ru.otus.hw.models.entity.Author;
import ru.otus.hw.models.entity.Book;
import ru.otus.hw.models.entity.Comment;
import ru.otus.hw.models.entity.Genre;
import ru.otus.hw.repositories.AuthorRepository;
import ru.otus.hw.repositories.BookRepository;
import ru.otus.hw.repositories.CommentRepository;
import ru.otus.hw.repositories.GenreRepository;

import java.util.List;


@ChangeLog
public class DatabaseChangelog {
    @ChangeSet(order = "001", id = "dropDb", author = "gulash", runAlways = true)
    public void dropDb(MongoDatabase db) {
        db.drop();
    }

    @ChangeSet(order = "002", id = "insert", author = "gulash")
    public void insertInit(
        AuthorRepository authorRepository,
        GenreRepository genreRepository,
        BookRepository bookRepository,
        CommentRepository commentRepository
    ) {
        Author author1 = new Author("1", "Dostoyevskiy_1");
        Author author2 = new Author("2", "Murakami_2");
        Author author3 = new Author("3", "Mayn_Rid_3");
        authorRepository.saveAll(List.of(author1, author2, author3));

        Genre genre1 = new Genre("1", "Genre_1");
        Genre genre2 = new Genre("2", "Genre_2");
        Genre genre3 = new Genre("3", "Genre_3");
        genreRepository.saveAll(List.of(genre1, genre2, genre3));

        Book book1 = new Book("1", "BookTitle_1", author1, genre1);
        Book book2 = new Book("2", "BookTitle_2", author2, genre2);
        Book book3 = new Book("3", "BookTitle_3", author3, genre3);
        bookRepository.saveAll(List.of(book1, book2, book3));

        Comment comment1Book1 = new Comment("1", "Comment_1_book_1", book1);
        Comment comment2Book1 = new Comment("2", "Comment_2_book_1", book1);
        Comment comment2Book11 = new Comment("3", "Comment_2_book_1", book1);
        Comment comment1Book2 = new Comment("1", "Comment_1_book_2", book2);
        Comment comment2Book2 = new Comment("2", "Comment_2_book_2", book2);
        commentRepository.saveAll(
            List.of(
                comment1Book1, comment2Book1, comment2Book11,
                comment1Book2, comment2Book2
            )
        );
    }
}
