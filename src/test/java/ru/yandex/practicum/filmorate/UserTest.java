package ru.yandex.practicum.filmorate;

import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import org.junit.jupiter.api.Test;
import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserTest {

    @Test
    void testValidUser() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("john_doe");
        user.setName("John Doe");
        user.setBirthday(LocalDate.of(1990, 5, 20));

        assertDoesNotThrow(() -> validateUser(user));
    }

    @Test
    void testEmailIsEmpty() {
        User user = new User();
        user.setEmail("");
        user.setLogin("john_doe");
        user.setName("John Doe");
        user.setBirthday(LocalDate.of(1990, 5, 20));

        ValidationException exception = assertThrows(ValidationException.class, () -> validateUser(user));
        assertEquals("Электронная почта не может быть пустой и должна содержать символ @.", exception.getMessage());
    }

    @Test
    void testLoginContainsSpace() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("john doe");
        user.setName("John Doe");
        user.setBirthday(LocalDate.of(1990, 5, 20));

        ValidationException exception = assertThrows(ValidationException.class, () -> validateUser(user));
        assertEquals("Логин не может быть пустым и содержать пробелы.", exception.getMessage());
    }

    @Test
    void testBirthdayInFuture() {
        User user = new User();
        user.setEmail("user@example.com");
        user.setLogin("john_doe");
        user.setName("John Doe");
        user.setBirthday(LocalDate.now().plusDays(1));

        ValidationException exception = assertThrows(ValidationException.class, () -> validateUser(user));
        assertEquals("Дата рождения не может быть в будущем.", exception.getMessage());
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
}