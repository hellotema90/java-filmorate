package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.FilmDbStorage;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.ResourceException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class FilmDbStorageTest {
    private final FilmDbStorage filmDbStorage;
    private final UserDbStorage userDbStorage;
    private User user1;
    private User user2;
    private User user3;
    private Film film1;
    private Film film2;

    @BeforeEach
    public void beforeEach() {
        film1 = Film.builder()
                .id(1)
                .name("фильм1")
                .description("описание1")
                .releaseDate(LocalDate.of(2010, 10, 10))
                .duration(80L)
                .rate("1")
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(new TreeSet<>())
                .build();
        filmDbStorage.addFilm(film1);
        film2 = Film.builder()
                .id(1)
                .name("фильм2")
                .description("описание2")
                .releaseDate(LocalDate.of(2010, 12, 12))
                .duration(80L)
                .rate("1")
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(new TreeSet<>())
                .build();
        filmDbStorage.addFilm(film2);
    }

    @AfterEach
    public void clearEach() {
        filmDbStorage.deleteAllFilms();
        userDbStorage.deleteAllUsers();
    }


    @Test
    void getFilmsTest() {
        List<Film> expected = new ArrayList<>();
        expected.add(film1);
        expected.add(film2);
        List<Film> actual = filmDbStorage.getAllFilms();
        assertEquals(expected, actual, "Списки фильмов не совпадают");
    }

    @Test
    public void findFilmByIdTest() {
        assertDoesNotThrow(() -> filmDbStorage.getFilmById(film1.getId()));
    }

    @Test
    void isUpdateFilm() throws ValidationException {
        Film filmUpd = Film.builder()
                .id(1)
                .name("фильм2")
                .description("описание2")
                .releaseDate(LocalDate.of(2010, 12, 12))
                .duration(80L)
                .rate("1")
                .mpa(Mpa.builder().id(1).name("G").build())
                .genres(new TreeSet<>())
                .build();
        filmDbStorage.addFilm(filmUpd);
        Film actualFilm = filmDbStorage.updateFilm(filmUpd);
        assertEquals(filmUpd, actualFilm, "Фильмы не совпадают");
    }

    @Test
    void addLikeFilmsTest() {
        user1 = User.builder()
                .name("имя1")
                .login("логин1")
                .email("email1@test.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userDbStorage.addUser(user1);
        user2 = User.builder()
                .name("имя2")
                .login("логин2")
                .email("email2@test.ru")
                .birthday(LocalDate.of(2000, 2, 2))
                .build();
        userDbStorage.addUser(user2);
        user3 = User.builder()
                .name("имя3")
                .login("логин3")
                .email("email3@test.ru")
                .birthday(LocalDate.of(2000, 3, 3))
                .build();
        userDbStorage.addUser(user3);
        filmDbStorage.addLike(film1.getId(), user1);
        filmDbStorage.addLike(film2.getId(), user1);
        filmDbStorage.addLike(film2.getId(), user2);
        List<Film> expected = new ArrayList<>();
        expected.add(film2);
        expected.add(film1);
        List<Film> popularFilms = filmDbStorage.getTopsFilms(2);
        assertEquals(expected, popularFilms, "Списки не совпадают");
    }


    @Test
    public void deleteFilmByIdTest() throws ValidationException {
        filmDbStorage.deleteFilmById(film1.getId());
        assertThrows(ResourceException.class, () -> {
            filmDbStorage.getFilmById(1);
        });
    }

    @Test
    void deleteFilmsTest() {
        List<Film> expectedList = new ArrayList<>();
        filmDbStorage.deleteAllFilms();
        List<Film> actualListFilms = filmDbStorage.getAllFilms();
        assertEquals(expectedList, actualListFilms, "Список не пустой");
    }
}
