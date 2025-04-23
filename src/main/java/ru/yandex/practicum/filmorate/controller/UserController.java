package ru.yandex.practicum.filmorate.controller;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.springframework.web.bind.annotation.*;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {

    private final List<User> users = new ArrayList<>();
    private int idCounter = 1;

    @PostMapping
    public User createUser(@RequestBody User user) {
        try {
            validateUser(user);
            user.setId(idCounter++);
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

    @PutMapping("/{id}")
    public User updateUser(@PathVariable int id, @RequestBody User updatedUser) {
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
            log.warn("Пользователь с ID {} не найден", id);
            return null;
        } catch (ValidationException e) {
            log.error("Ошибка валидации при обновлении пользователя: {}", e.getMessage());
            throw e;
        }
    }

    @GetMapping
    public List<User> getAllUsers() {
        log.info("Запрос на получение всех пользователей");
        return users;
    }
}