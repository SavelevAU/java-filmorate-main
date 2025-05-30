package ru.yandex.practicum.filmorate.service;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;


import java.util.Collection;
import java.util.Set;

@Slf4j
@Service
@RequiredArgsConstructor
public class FilmService {

    private final FilmStorage filmStorage;
    private final UserService userService;


    public Collection<Film> getAllFilms() {
        return filmStorage.getAllFilms();
    }

    public Film addFilm(Film film) {
        return filmStorage.addFilm(film);
    }

    public Film updateFilm(Film updatedFilm) {
        return filmStorage.updateFilm(updatedFilm);
    }

    public Film findFilmById(Integer id) {
        return filmStorage.getAllFilms().stream()
                .filter(film -> film.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Фильм с id %d не найден", id)));
    }

    public void addLikeByUser(Integer filmId, Integer userId) {
        log.debug("Вызван метод addLikeByUser filmId = {}, userId = {}", filmId, userId);
        Film film = findFilmById(filmId);
        User user = userService.findUserById(userId);

        Set<Integer> likes = film.getLikes();
        likes.add(user.getId());
        film.setLikes(likes);
    }

    public void deleteLikeByUser(Integer filmId, Integer userId) {
        log.debug("Вызван метод deleteLikeByUser filmId = {}, userId = {}", filmId, userId);
        Film film = findFilmById(filmId);
        User user = userService.findUserById(userId);

        Set<Integer> likes = film.getLikes();
        likes.remove(user.getId());
        film.setLikes(likes);
    }

    public Collection<Film> getPopularFilms(int count) {
        log.debug("Вызван метод getPopularFilms count = {}", count);
        return filmStorage.getAllFilms()
                .stream()
                .sorted(Film.byLikesCountDesc())
                .limit(count)
                .toList();
    }
}