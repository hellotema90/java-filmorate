package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.Mpa;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;
import java.util.TreeSet;

import static org.junit.jupiter.api.Assertions.*;


class FilmControllerTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private Film film1;
    @Autowired
    FilmController filmController;

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
