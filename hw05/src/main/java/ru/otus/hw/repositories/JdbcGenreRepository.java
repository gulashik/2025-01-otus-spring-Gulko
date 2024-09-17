package ru.otus.hw.repositories;

import lombok.RequiredArgsConstructor;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.core.namedparam.NamedParameterJdbcTemplate;
import org.springframework.stereotype.Repository;
import ru.otus.hw.models.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@RequiredArgsConstructor
@Repository
public class JdbcGenreRepository implements GenreRepository {

    private final NamedParameterJdbcTemplate namedJdbc;

    @Override
    public List<Genre> findAll() {
        return namedJdbc.query("select id, name from genres", new GenreRowMapper());
    }

    @Override
    public Optional<Genre> findById(long id) {
        return namedJdbc.query(
                        "select id, name from genres where id = :id",
                        Map.of("id", id),
                        new GenreRowMapper()
                ).stream().findFirst();
    }

    @Override
    public Genre save(Genre genre) {
        namedJdbc.update(
                "merge into genres(id, name) values (:id, :name)",
                Map.of("id", genre.getId(), "name", genre.getName())
        );

        return genre;
    }

    private static class GenreRowMapper implements RowMapper<Genre> {

        @Override
        public Genre mapRow(ResultSet rs, int i) throws SQLException {
            return new Genre(
                rs.getLong("id"),
                rs.getString("name")
            );
        }
    }
}
