package ru.yandex.practicum.filmorate.dto;


import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;

import java.time.LocalDate;

@Data
@Builder(toBuilder = true)
public class NewUserRequest {
    private Long id;
    @Email
    @NotNull
    @NotBlank
    private String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелов")
    private String login;
    private String name;
    @PastOrPresent
    private LocalDate birthday;
}