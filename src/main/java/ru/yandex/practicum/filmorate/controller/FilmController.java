package ru.yandex.practicum.filmorate.controller;

import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import ru.yandex.practicum.filmorate.dto.FilmDto;
import ru.yandex.practicum.filmorate.dto.NewFilmRequest;
import ru.yandex.practicum.filmorate.dto.UpdateFilmRequest;
import ru.yandex.practicum.filmorate.exception.*;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.service.FilmService;

import java.util.Collection;

@RestController
@RequestMapping("/films")
@Slf4j
@RequiredArgsConstructor
public class FilmController {

    private final FilmService filmService;


    @GetMapping
    public Collection<FilmDto> getAllFilms() {
        return filmService.getAllFilms();
    }

    @PostMapping
    public FilmDto addFilm(@Valid@RequestBody NewFilmRequest film) {
        return filmService.addFilm(film);
    }

    @PutMapping
    public FilmDto updateFilm(@Valid @RequestBody UpdateFilmRequest updatedFilm) {
        return filmService.updateFilm(updatedFilm);
    }


    @GetMapping("/{id}")
    public FilmDto findFilmById(@PathVariable Long id) {
        return filmService.findFilmById(id);
    }

    @PutMapping("{id}/like/{userId}")
    public void addLikeByUser(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        filmService.addLikeByUser(filmId, userId);
    }

    @DeleteMapping("{id}/like/{userId}")
    public void deleteLikeByUser(@PathVariable("id") Long filmId, @PathVariable Long userId) {
        filmService.deleteLikeByUser(filmId, userId);
    }

    @GetMapping("popular")
    public Collection<FilmDto> getPopularFilms(@RequestParam(defaultValue = "10") int count) {
        if (count <= 0) {
            throw new ValidationException("Размер должен быть больше нуля");
        }
        return filmService.getPopularFilms(count);
    }


}