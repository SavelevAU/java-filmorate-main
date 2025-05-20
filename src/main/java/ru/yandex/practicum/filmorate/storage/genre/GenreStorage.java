package ru.yandex.practicum.filmorate.storage.genre;

import ru.yandex.practicum.filmorate.model.Genre;

import java.util.Collection;
import java.util.Optional;

public interface GenreStorage {

    Collection<Genre> findAll();

    Optional<Genre> findById(Long id);

    void createFilmGenres(Long filmId, Long genreId);

    void deleteFilmGenres(Long filmId);

    Collection<Genre> findAllByFilmId(Long filmId);

}