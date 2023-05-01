package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;


class FilmControllerTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private Film film1;
    private FilmController filmController;

    @BeforeEach
    public void beforeEach() {
        filmController = new FilmController();
        film1 = new Film(1, "фильм1", "описание1",
                LocalDate.of(2010, 10, 10), 80L);
    }

    @Test
    void postFilm() {
        filmController.post(film1);
        assertEquals(1, filmController.findAllFilms().size());
        Film film2 = new Film(2, "фильм2", "описание2",
                LocalDate.of(2010, 10, 10), 80L);
        filmController.post(film2);
        assertEquals(2, filmController.findAllFilms().size());
    }

    @Test
    void putFilm() {
        filmController.post(film1);
        assertEquals(film1.getDescription(), "описание1");
        film1.setDescription("описание2");
        filmController.put(film1);
        assertEquals(film1.getDescription(), "описание2");
    }

    @Test
    void postFilmWithBlankName() {
        film1.setName("");
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertEquals(1, violations.size(), "Валидация не корректна, название не может быть пустым");
    }

    @Test
    void postFilmMoreThanMaxSize() {
        film1.setDescription("описание2описание2описание2описание2описание2описание2описание2" +
                "описание2описание2описание2описание2описание2описание2описание2описание2описание2" +
                "описание2описание2описание2описание2описание2описание2описание2описание2описание2");
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertEquals(1, violations.size(), "Валидация не корректна, максимальная длина описания" +
                " — 200 символов");
    }

    @Test
    void postFilmWithNullReleaseDate() {
        film1.setReleaseDate(null);
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertEquals(1, violations.size(), "Валидация не корректна, дата релиза не может быть равна 0");
    }

    @Test
    void postFilmWithNullDuration() {
        film1.setDuration(0);
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertEquals(1, violations.size(), "Валидация не корректна, продолжительность" +
                " не может быть равна 0");
    }

    @Test
    void postFilmWithNoPositiveDuration() {
        film1.setDuration(-10);
        Set<ConstraintViolation<Film>> violations = validator.validate(film1);
        assertEquals(1, violations.size(), "Валидация не корректна, продолжительность" +
                " не может быть меньше ");
    }
}