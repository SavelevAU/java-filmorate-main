package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.*;
import ru.yandex.practicum.filmorate.validation.*;

import java.time.LocalDate;
import java.util.Comparator;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class Film {
    @EqualsAndHashCode.Include
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

    public Set<Long> getLikes() {
        if (likes == null) {
            return new HashSet<>();
        }
        return likes;
    }

    public static Comparator<Film> byLikesCount() {
        return Comparator.comparingInt(film -> film.getLikes() != null ? film.getLikes().size() : 0);
    }

    public static Comparator<Film> byLikesCountDesc() {
        return byLikesCount().reversed();
    }
}