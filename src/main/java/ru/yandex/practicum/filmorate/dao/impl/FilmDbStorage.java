package ru.yandex.practicum.filmorate.dao.impl;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.dao.DataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.jdbc.core.RowMapper;
import org.springframework.jdbc.support.rowset.SqlRowSet;
import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ResourceException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.FilmStorage;

import java.sql.ResultSet;
import java.sql.SQLException;
import java.time.LocalDate;
import java.util.List;
import java.util.Set;
import java.util.TreeSet;

@Slf4j
@Component("filmDao")
@RequiredArgsConstructor
public class FilmDbStorage implements FilmStorage {

    private final JdbcTemplate jdbcTemplate;

    @Override
    public void addFilm(Film film) throws ValidationException {
        checkNameFilm(film);
        String sql = "INSERT INTO FILM (NAME, DESCRIPTION, RELEASEDATE, DURATION, RATE) "
                + "VALUES ((?), (?), (?), (?), (?))";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate());

        SqlRowSet filmRows = jdbcTemplate.queryForRowSet("select * from FILM where NAME=?", film.getName());
        if (filmRows.next()) {
            film.setId(filmRows.getInt("id"));
        }
        validFilm(film);
        log.info("Фильм {} добавлен", film);
    }

    public void validFilm(Film film) {
        if (film.getMpa() != null) {
            String sqlForMpa = "INSERT INTO FILM_MPA (FILM_ID, MPA_ID) VALUES ((?), (?))";
            jdbcTemplate.update(sqlForMpa,
                    film.getId(),
                    film.getMpa().getId());
        }
        if (film.getGenres() != null) {
            String sqlForGenre = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES ((?), (?))";
            for (Genre genre : film.getGenres()) {
                jdbcTemplate.update(sqlForGenre,
                        film.getId(),
                        genre.getId());
            }
        }
    }

    @Override
    public Film updateFilm(Film film) throws ValidationException {
        checkId(film.getId());
        checkNameFilm(film);
        String sql = "UPDATE FILM SET NAME=?, DESCRIPTION=?, RELEASEDATE=?, DURATION=?, RATE=?"
                + " WHERE ID = ?";
        jdbcTemplate.update(sql,
                film.getName(),
                film.getDescription(),
                film.getReleaseDate(),
                film.getDuration(),
                film.getRate(),
                film.getId());

        if (film.getGenres() != null) {
            if (!film.getGenres().isEmpty()) {
                String sqlGD = "DELETE FROM FILM_GENRE WHERE FILM_ID=?";
                jdbcTemplate.update(sqlGD, film.getId());
                String sqlG = "INSERT INTO FILM_GENRE (FILM_ID, GENRE_ID) VALUES ((?), (?))";
                for (Genre genre : film.getGenres()) {
                    jdbcTemplate.update(sqlG, film.getId(), genre.getId());
                }
                film.setGenres(new TreeSet<>(film.getGenres()));
            } else {
                String sqlG = "DELETE FROM FILM_GENRE WHERE FILM_ID=?";
                jdbcTemplate.update(sqlG, film.getId());
            }
        }

        if (film.getMpa() != null) {
            if (film.getMpa().getId() > 0) {
                String sqlForMpa = "DELETE FROM FILM_MPA WHERE FILM_ID=?";
                jdbcTemplate.update(sqlForMpa, film.getId());
                String sqlForMpaInsert = "INSERT INTO FILM_MPA (FILM_ID, MPA_ID) VALUES ((?), (?))";
                jdbcTemplate.update(sqlForMpaInsert, film.getId(), film.getMpa().getId());
            } else {
                String sqlForMpa = "DELETE FROM FILM_MPA WHERE FILM_ID=?";
                jdbcTemplate.update(sqlForMpa, film.getId());
            }
        }
        log.info("Фильм {} обновлен", film);
        return film;
    }

    @Override
    public List<Film> getAllFilms() {
        String sql = "select * from FILM";
        return jdbcTemplate.query(sql, this::mapRowToFilm);
    }

    @Override
    public Film getFilmById(int id) {
        try {
            String sqlQuery = "select * from FILM where ID = ?";
            return jdbcTemplate.queryForObject(sqlQuery, this::mapRowToFilm, id);
        } catch (DataAccessException dataAccessException) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Пользователь с таким id не найден.");
        }
    }

    @Override
    public void addLike(int idFilm, User user) {
        String sql = "INSERT INTO LIKES(USER_ID, FILM_ID) VALUES ((?), (?))";
        jdbcTemplate.update(sql, user.getId(), idFilm);
        log.info("Пользователь {} поставил лайк фильму {}", user, idFilm);
    }

    @Override
    public void deleteLike(int idFilm, User user) throws ValidationException {
        checkId(idFilm);
        String sql = "DELETE FROM LIKES WHERE USER_ID=? AND FILM_ID=?";
        jdbcTemplate.update(sql, user.getId(), idFilm);
        log.info("Пользователь {} отменил лайк фильму {}", user, idFilm);
    }

    @Override
    public List<Film> getTopsFilms(Integer count) {
        String sql = "SELECT F.ID, F.NAME, F.DESCRIPTION, F.RELEASEDATE, F.DURATION, F.RATE "
                + "FROM FILM F LEFT JOIN LIKES L on F.ID = L.FILM_ID GROUP BY F.ID "
                + "ORDER BY COUNT(L.USER_ID) DESC LIMIT ?";
        return jdbcTemplate.query(sql, this::mapRowToFilm, count);
    }

    @Override
    public void deleteAllFilms() {
        String sql = "DELETE from FILM";
        jdbcTemplate.update(sql);
        log.info("Удалены все фильмы таблицы FILM");
    }

    @Override
    public void deleteFilmById(Integer id) throws ValidationException {
        checkId(id);
        String sql = "DELETE from FILM where ID=?";
        jdbcTemplate.update(sql, id);
    }

    private Film mapRowToFilm(ResultSet resultSet, int rowNum) throws SQLException {
        return Film.builder()
                .id(resultSet.getInt("id"))
                .description(resultSet.getString("description"))
                .duration(resultSet.getInt("duration"))
                .rate(resultSet.getString("rate"))
                .name(resultSet.getString("name"))
                .releaseDate(resultSet.getDate("releasedate").toLocalDate())
                .genres(getGenresForFilm(resultSet.getInt("id")))
                .mpa(getMpaForFilm(resultSet.getInt("ID")))
                .build();
    }

    private Mpa getMpaForFilm(int idFilm) {
        String sql =
                "SELECT M2.ID, M2.NAME "
                        + "FROM FILM F "
                        + "LEFT JOIN FILM_MPA FM on F.ID = FM.film_id "
                        + "LEFT JOIN MPA M2 on FM.mpa_id = M2.ID "
                        + "WHERE F.ID = ?";

        RowMapper<Mpa> rowMapper = (rs, rowNum) ->
                Mpa.builder()
                        .id(rs.getInt(1))
                        .name(rs.getString(2))
                        .build();

        List<Mpa> mpa = jdbcTemplate.query(sql, rowMapper, idFilm);

        if (mpa.isEmpty()) {
            throw new ResourceException(HttpStatus.OK, "у фильма пока нет MPA.");
        } else {
            return mpa.get(0);
        }
    }

    private Set<Genre> getGenresForFilm(int idFilm) {
        SqlRowSet genreRows = jdbcTemplate.queryForRowSet(
                "SELECT G2.ID, G2.NAME "
                        + "FROM FILM F "
                        + "LEFT JOIN FILM_GENRE FG on F.ID = FG.film_id "
                        + "LEFT JOIN GENRE G2 on FG.genre_id = G2.ID "
                        + "WHERE F.ID =?", idFilm);
        Set<Genre> genresSet = new TreeSet<>();
        while (genreRows.next()) {
            Genre genre = Genre.builder()
                    .id(genreRows.getInt("id"))
                    .name(genreRows.getString("name"))
                    .build();
            if (genre.getId() > 0) {
                genresSet.add(genre);
            }
        }
        return genresSet;
    }

    private void checkNameFilm(Film film) throws ValidationException {
        LocalDate movieBirthday = LocalDate.parse("1895-12-28");
        if (film.getName().isBlank() || film.getName().isEmpty()
                || film.getReleaseDate().isBefore(movieBirthday)) {
            throw new ValidationException("Фильм не соответствует условиям. " +
                    "Проверьте данные и повторите запрос.");
        }
    }

    private void checkId(int id) throws ValidationException {
        if (getFilmById(id) == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Фильм с таким id не найден.");
        } else if (id < 0) {
            throw new ValidationException("Отрицательные значения не допустимы.");
        }
    }
}