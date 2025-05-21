package ru.yandex.practicum.filmorate.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.Builder;
import lombok.Data;
import ru.yandex.practicum.filmorate.model.Genre;
import ru.yandex.practicum.filmorate.model.Rating;
import ru.yandex.practicum.filmorate.validation.*;

import java.time.LocalDate;
import java.util.Set;

@Data
@Builder(toBuilder = true)
public class NewFilmRequest {
    private Long id;
    @NotNull
    @NotBlank
    private String name;
    @Size(max = 200, message = "Размер описания не должен превышать 200 символов")
    private String description;
    @NotBeforeDate()
    private LocalDate releaseDate;
    @Positive(message = "Длительность должна быть положительной")
    private Integer duration;
    private Set<Long> likes;
    private Set<Genre> genres;
    private Rating mpa;

    public boolean hasGenres() {
        return genres != null;
    }
}