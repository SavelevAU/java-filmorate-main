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
    private Integer id;
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
    private  LocalDate birthday;
    private Set<Integer> friends;

    public Set<Integer> getFriends() {
        if (friends == null) {
            return new HashSet<>();
        }
        return friends;
    }
}