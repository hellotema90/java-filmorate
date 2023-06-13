package ru.yandex.practicum.filmorate.service;

import ru.yandex.practicum.filmorate.dao.GenreDao;
import ru.yandex.practicum.filmorate.model.Genre;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.Set;

@Service
public class GenreService {
    private final GenreDao genreDao;

    @Autowired
    public GenreService(GenreDao genreDao) {
        this.genreDao = genreDao;
    }

    public Set<Genre> getGenresService() {
        return genreDao.getGenres();
    }

    public Genre getGenresFromFilmService(String igFilm) {
        return genreDao.getGenresById(igFilm);
    }
}