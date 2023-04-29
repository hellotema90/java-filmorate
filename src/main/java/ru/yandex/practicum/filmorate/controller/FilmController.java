package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;

import javax.validation.Valid;

import java.time.LocalDate;
import java.util.*;

@Validated
@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final Map<Integer, Film> films = new HashMap<>();
    private int generatorId = 0;

    //счетчик для выдачи уникального id
    private int countGeneratorId() {
        return ++generatorId;
    }


    @GetMapping
    public Collection<Film> findAllFilms() {
        log.info("Получить список всех фильмов");
        return films.values();
    }

    @PostMapping
    public Film post(@Valid @RequestBody Film film) {
        if (validateFilm(film)) {
            films.put(film.setId(countGeneratorId()), film);
            log.info("Добавлен фильм {}", film);
        }
        return film;
    }

    @PutMapping
    public Film put(@Valid @RequestBody Film film) {
        if (films.get(film.getId()).getId() == film.getId()) {
            if (validateFilm(film)) {
                films.put(film.setId(film.getId()), film);
                log.info("Изменен фильм {}", film);
            }
        }
        return film;
    }

    private boolean validateFilm(Film film) {
        if (film.getDescription().length() > 200) {
            log.warn("максимальная длина описания — 200 символов");
            throw new ValidationException("максимальная длина описания — 200 символов");
        }
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            log.warn("дата релиза — не раньше 28 декабря 1895 года");
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        return true;
    }
}

