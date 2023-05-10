package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface FilmStorage {
    void addFilm(Film film);

    void updateFilm(Film film);

    List<Film> getAllFilms();

    Film getFilmById(int id);

    void addLike(int idFilm, User user);

    void deleteLike(int idFilm, User user);
}
