package ru.yandex.practicum.filmorate.dao;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Set;

public interface GenreDao {

    Set<Genre> getGenres();

    Genre getGenresById(String idFilm);
}