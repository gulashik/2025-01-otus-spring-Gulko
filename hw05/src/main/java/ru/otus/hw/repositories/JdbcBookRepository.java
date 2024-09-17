package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.MapSqlParameterSource;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.jdbc.support.GeneratedKeyHolder;
import org.springframework.stereotype.Repository;
import org.springframework.transaction.support.TransactionTemplate;
import ru.otus.hw.models.Author;
import ru.otus.hw.models.Book;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JdbcBookRepository implements BookRepository {

    private final NamedParameterJdbcTemplate namedJdbc;

    private final TransactionTemplate transactionTemplate;

    private final AuthorRepository authorRepository;

    private final GenreRepository genreRepository;

    private final String BOOK_SQL = """
            select
              b.id, b.title,
              b.genre_id, g.name as genre_name,
              b.author_id, a.full_name as author_full_name
            from 
              books b 
              left join genres g 
                  on b.genre_id = g.id 
              left join authors a 
                  on b.author_id = a.id
          """;

    @Override
    public Optional<Book> findById(long id) {
        String sql = BOOK_SQL + "\n where b.id = :id";

        return namedJdbc.query(sql, new MapSqlParameterSource("id", id), new BookRowMapper())
                .stream().findFirst();
    }

    @Override
    public List<Book> findAll() {
        return namedJdbc.query(BOOK_SQL, new BookRowMapper());
    }

    @Override
    public Book save(Book book) {
        if (book.getId() == 0) {
            return insert(book);
        }
        return update(book);
    }

    @Override
    public void deleteById(long id) {
        namedJdbc.update("delete books where id = :id", Map.of("id", id));
    }

    private Book insert(Book book) {
        return transactionTemplate.execute(
            status -> {
                // obey referential integrity
                genreRepository.save(book.getGenre());
                authorRepository.save(book.getAuthor());

                GeneratedKeyHolder keyHolderBook = new GeneratedKeyHolder();
                MapSqlParameterSource bookParams = new MapSqlParameterSource() {{
                    addValue("title", book.getTitle());
                    addValue("author_id", book.getAuthor().getId());
                    addValue("genre_id", book.getGenre().getId());
                }};

                namedJdbc.update(
                        "insert into books(title, author_id, genre_id) values(:title, :author_id, :genre_id)",
                        bookParams,
                        keyHolderBook
                );
                book.setId(keyHolderBook.getKeyAs(Long.class));
                return book;
            }
        );
    }

    private Book update(Book book) {
        return transactionTemplate.execute(
            status -> {
                // obey referential integrity
                genreRepository.save(book.getGenre());
                authorRepository.save(book.getAuthor());

                namedJdbc.update(
                    "merge into books(id, title, author_id, genre_id) values (:id, :title,:author_id, :genre_id)",
                    Map.of("id", book.getId(), "title", book.getTitle(),
                            "author_id", book.getAuthor().getId(), "genre_id", book.getGenre().getId())
                );
                return book;
            }
        );
    }

    private static class BookRowMapper implements RowMapper<Book> {
        @Override
        public Book mapRow(ResultSet rs, int rowNum) throws SQLException {
            Author author = new Author(
                    rs.getLong("author_id"),
                    rs.getString("author_full_name")
            );
            Genre genre = new Genre(
                    rs.getLong("genre_id"),
                    rs.getString("genre_name")
            );
            return new Book(
                rs.getLong("id"),
                rs.getString("title"),
                author,
                genre
            );
        }
    }
}
