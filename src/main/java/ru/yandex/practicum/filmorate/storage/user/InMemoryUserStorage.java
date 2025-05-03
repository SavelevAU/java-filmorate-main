package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;
import ru.yandex.practicum.filmorate.storage.film.FilmStorage;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final List<User> users = new ArrayList<>();
    private Integer idCounter = 1;

    @Override
    public User createUser(@RequestBody User user) {
        try {
            validateUser(user);
            user.setId(idCounter++);
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.add(user);
            log.info("Пользователь успешно создан: {}", user.getLogin());
            return user;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при создании пользователя: {}", e.getMessage());
            throw e;
        }
    }

    private void validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            throw new ValidationException("Электронная почта не может быть пустой и должна содержать символ @.");
        }
        if (user.getLogin() == null || user.getLogin().isEmpty() || user.getLogin().contains(" ")) {
            throw new ValidationException("Логин не может быть пустым и содержать пробелы.");
        }
        if (user.getBirthday() != null && user.getBirthday().isAfter(LocalDate.now())) {
            throw new ValidationException("Дата рождения не может быть в будущем.");
        }

    }

    @Override
    public User updateUser(@RequestBody User updatedUser) {
        try {
            validateUser(updatedUser);
            for (User user : users) {
                if (user.getId() == updatedUser.getId()) {
                    user.setEmail(updatedUser.getEmail());
                    user.setLogin(updatedUser.getLogin());
                    user.setName(updatedUser.getName());
                    user.setBirthday(updatedUser.getBirthday());
                    log.info("Пользователь успешно обновлен: {}", user.getLogin());
                    return user;
                }
            }
            log.warn("Пользователь с ID {} не найден", updatedUser.getId());
            throw new NotFoundException("Пользователь с id = " + updatedUser.getId() + " не найден");
            //return null;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении пользователя: {}", e.getMessage());
            throw e;
        }
    }

    @Override
    public List<User> getAllUsers() {
        log.info("Запрос на получение всех пользователей");
        return users;
    }
}
