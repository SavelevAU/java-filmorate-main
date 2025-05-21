package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import lombok.EqualsAndHashCode;

@Data
public class Genre {
    @EqualsAndHashCode.Include
    private Long id;
    @NotNull
    @NotBlank
    private String name;
}