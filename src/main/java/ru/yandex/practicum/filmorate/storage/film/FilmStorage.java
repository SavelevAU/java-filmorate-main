package ru.yandex.practicum.filmorate.storage.film;

import ru.yandex.practicum.filmorate.model.Film;
import java.util.Collection;
import java.util.Optional;

public interface FilmStorage {

    Film addFilm(Film film);

    Film updateFilm(Film updatedFilm);

    Collection<Film> getAllFilms();

    Optional<Film> findFilmById(Long id);

    void addLikeByUser(Long filmId, Long userId);

    void deleteLikeByUser(Long filmId, Long userId);

    Collection<Film> getPopularFilms(int count);

}