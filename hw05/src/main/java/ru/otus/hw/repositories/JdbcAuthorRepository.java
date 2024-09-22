package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Author;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JdbcAuthorRepository implements AuthorRepository {

    private final NamedParameterJdbcTemplate namedJdbc;

    @Override
    public List<Author> findAll() {
        return namedJdbc.query("select id, full_name from authors", new AuthorRowMapper());
    }

    @Override
    public Optional<Author> findById(long id) {
        return namedJdbc.query(
                        "select id, full_name from authors where id = :id",
                        Map.of("id", id),
                        new AuthorRowMapper()
                )
                .stream().findFirst();
    }

    @Override
    public Author save(Author author) {
         namedJdbc.update(
                "merge into authors(id, full_name) values (:id, :full_name)",
                Map.of("id", author.getId(), "full_name", author.getFullName())
        );
         return author;
    }

    private static class AuthorRowMapper implements RowMapper<Author> {

        @Override
        public Author mapRow(ResultSet rs, int i) throws SQLException {
            return new Author(
                rs.getLong("id"),
                rs.getString("full_name")
            );
        }
    }
}
