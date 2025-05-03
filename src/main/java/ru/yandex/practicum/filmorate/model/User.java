package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;

import java.time.LocalDate;
import java.util.HashSet;
import java.util.Set;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
public class User {
    @EqualsAndHashCode.Include
    Integer id;
    @Email
    @NotNull
    @NotBlank
    String email;
    @NotNull
    @NotBlank
    @Pattern(regexp = "\\S+", message = "Логин не должен содержать пробелов")
    String login;
    String name;
    @PastOrPresent
    LocalDate birthday;
    Set<Integer> friends;

    public Set<Integer> getFriends() {
        if (friends == null) {
            return new HashSet<>();
        }
        return friends;
    }
}