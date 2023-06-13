package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.exception.ResourceException;
import ru.yandex.practicum.filmorate.model.Genre;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Component
@RequiredArgsConstructor
public class GenreDaoImpl implements GenreDao {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public Set<Genre> getGenres() {
        String sql = ("SELECT * FROM GENRE");
        return new TreeSet<>(jdbcTemplate.query(sql, this::mapRowToGenre));
    }

    @Override
    public Genre getGenresById(String idGenre) {
        String sql = "SELECT * FROM GENRE WHERE ID = ?";
        List<Genre> genres = jdbcTemplate.query(sql, this::mapRowToGenre, idGenre);

        if (genres.isEmpty()) {
            log.info("жанр с идентификатором {} не найден.", idGenre);
            throw new ResourceException(HttpStatus.NOT_FOUND, "Такого жанра нет в базе.");
        } else {
            return genres.get(0);
        }
    }

    private Genre mapRowToGenre(ResultSet resultSet, int rowNum) throws SQLException {
        return Genre.builder()
                .id(resultSet.getInt("id"))
                .name(resultSet.getString("name"))
                .build();
    }
}
