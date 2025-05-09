package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.Film;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/films")
@Slf4j
public class FilmController {

    private final List<Film> films = new ArrayList<>();
    private int idCounter = 1;

    @PostMapping
    public Film addFilm(@RequestBody Film film) {
        try {
            validateFilm(film);
            film.setId(idCounter++);
            films.add(film);
            log.info("Фильм успешно добавлен: {}", film.getName());
            return film;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при добавлении фильма: {}", e.getMessage());
            throw e;
        }
    }

    private void validateFilm(Film film) {
        if (film.getName() == null || film.getName().isEmpty()) {
            throw new ValidationException("Название фильма не может быть пустым.");
        }
        if (film.getDescription() != null && film.getDescription().length() > 200) {
            throw new ValidationException("Максимальная длина описания — 200 символов.");
        }
        if (film.getReleaseDate() != null && film.getReleaseDate().isBefore(LocalDate.of(1895, 12, 28))) {
            throw new ValidationException("Дата релиза не может быть раньше 28 декабря 1895 года.");
        }
        if (film.getDuration() <= 0) {
            throw new ValidationException("Продолжительность фильма должна быть положительным числом.");
        }
    }

    @PutMapping
    public Film updateFilm(@RequestBody Film updatedFilm) {
        try {
            validateFilm(updatedFilm);
            for (Film film : films) {
                if (film.getId() == updatedFilm.getId()) {
                    film.setName(updatedFilm.getName());
                    film.setDescription(updatedFilm.getDescription());
                    film.setReleaseDate(updatedFilm.getReleaseDate());
                    film.setDuration(updatedFilm.getDuration());
                    log.info("Фильм успешно обновлен: {}", film.getName());
                    return film;
                }
            }
            log.warn("Фильм с ID {} не найден", updatedFilm.getId());
            throw new NotFoundException("Фильм с id = " + updatedFilm.getId() + " не найден");
     //       return null;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении фильма: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping
    public List<Film> getAllFilms() {
        log.info("Запрос на получение всех фильмов");
        return films;
    }
}