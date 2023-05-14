package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryFilmStorage implements FilmStorage {

    private final Map<Integer, Film> films = new HashMap<>();
    private final AtomicInteger generatorId = new AtomicInteger(0);

    private int countGeneratorId() {
        return generatorId.incrementAndGet();
    }

    @Override
    public void addFilm(Film film) {
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
