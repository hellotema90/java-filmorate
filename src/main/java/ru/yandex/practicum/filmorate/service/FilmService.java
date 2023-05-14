package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.storage.FilmStorage;
import ru.yandex.practicum.filmorate.storage.UserStorage;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.http.HttpStatus;
import ru.yandex.practicum.filmorate.exception.ResourceException;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class FilmService {
    @Autowired
    FilmStorage filmStorage;
    @Autowired
    UserStorage userStorage;

    public Film addFilm(Film film) throws ValidationException {
        if (film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("дата релиза — не раньше 28 декабря 1895 года");
        }
        filmStorage.addFilm(film);
        log.debug("Фильм успешно добавлен.");
        return film;
    }

    public Film updateFilm(Film film) {
        validateContainsId(film.getId());
        filmStorage.updateFilm(film);
        log.debug("Информация о фильме успешно обновлена.");
        return film;
    }

    public List<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film getFilmById(int id) {
        validateContainsId(id);
        return filmStorage.getFilmById(id);
    }

    public void addLIke(int idFilm, int userId) {
        validateContainsId(idFilm);
        log.debug("Лайк успешно поставлен.");
        filmStorage.addLike(idFilm, userStorage.getUserByID(userId));
    }

    public void deleteLike(int idFilm, int userId) {
        validateContainsId(idFilm);
        validateContainsIdUser(userId);
        log.debug("Лайк успешно удален.");
        filmStorage.deleteLike(idFilm, userStorage.getUserByID(userId));
    }

    public List<Film> getTopsFilms(Integer count) {
        return filmStorage.getAllFilms().stream()
                .sorted(Comparator.<Film>comparingInt(film -> film.getLikes().size()).reversed())
                .limit(count)
                .collect(Collectors.toList());
    }

    private void validateContainsId(int id) {
        if (filmStorage.getFilmById(id) == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Фильм с таким id не найден.");
        }
    }

    private void validateContainsIdUser(int id) {
        if (userStorage.getUserByID(id) == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Пользователь с таким id не найден.");
        }
    }
}

