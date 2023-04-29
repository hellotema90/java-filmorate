package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.Film;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class FilmControllerTest {
    FilmController filmController = new FilmController();

    @Test
    void postFilm() {
        assertEquals(0, filmController.findAllFilms().size());
        Film film1 = new Film(1, "фильм1", "описание1",
                LocalDate.of(2010, 10, 10), 80L);
        filmController.post(film1);
        assertEquals(1, filmController.findAllFilms().size());
        Film film2 = new Film(2, "фильм2", "описание2",
                LocalDate.of(2010, 10, 10), 80L);
        filmController.post(film2);
        assertEquals(2, filmController.findAllFilms().size());
    }

    @Test
    void putFilm() {
        Film film1 = new Film(1, "фильм1", "описание1",
                LocalDate.of(2010, 10, 10), 80L);
        filmController.post(film1);
        assertEquals(film1.getDescription(), "описание1");
        film1.setDescription("описание2");
        filmController.put(film1);
        assertEquals(film1.getDescription(), "описание2");
    }
}