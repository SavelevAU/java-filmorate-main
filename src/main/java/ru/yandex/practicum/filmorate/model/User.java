package ru.yandex.practicum.filmorate.model;

import jakarta.validation.constraints.*;
import lombok.*;

import java.time.LocalDate;
import java.util.HashMap;
import java.util.Map;

@Data
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Builder(toBuilder = true)
@AllArgsConstructor(access = AccessLevel.PUBLIC)
@NoArgsConstructor
public class User {
    @EqualsAndHashCode.Include
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
    private  LocalDate birthday;
    private Map<Long, Boolean> friends;

    public Map<Long, Boolean> getFriends() {
        if (friends == null) {
            return new HashMap<Long, Boolean>();
        }
        return friends;
    }
}