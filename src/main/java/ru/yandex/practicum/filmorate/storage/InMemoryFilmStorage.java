package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private int generatorId = 0;

    //счетчик для выдачи уникального id
    private int countGeneratorId() {
        return ++generatorId;
    }

    @Override
    public void addFilm(Film film) {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        film.setId(countGeneratorId());
        films.put(film.getId(), film);
    }

    @Override
    public void updateFilm(Film film) {
       films.put(film.getId(), film);
    }

    @Override
    public List<Film> getAllFilms() {
        return new ArrayList<>(films.values());
    }

    @Override
    public Film getFilmById(int id) {
        return films.get(id);
    }

    @Override
    public void addLike(int idFilm, User user) {
        films.get(idFilm).getLikes().put(user.getId(), user);
    }

    @Override
    public void deleteLike(int idFilm, User user) {
        films.get(idFilm).getLikes().remove(user.getId());
    }
}
