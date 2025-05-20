package ru.yandex.practicum.filmorate.storage.user;

import org.springframework.stereotype.Component;
import org.springframework.web.bind.annotation.RequestBody;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;
import lombok.extern.slf4j.Slf4j;

import java.time.LocalDate;
import java.util.*;

@Slf4j
@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Long, User> users = new HashMap<>();
    private Long idCounter = 1l;

    @Override
    public User createUser(@RequestBody User user) {
        try {
            validateUser(user);
            user.setId(idCounter++);
            if (user.getName() == null || user.getName().isBlank()) {
                user.setName(user.getLogin());
            }
            users.put(user.getId(), user);
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
            for (User user : users.values()) {
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
    public Collection<User> getAllUsers() {
        log.info("Запрос на получение всех пользователей");
        return users.values();
    }

    @Override
    public Optional<User> findById(Long id) {
        return Optional.ofNullable(users.get(id));
    }

    @Override
    public Collection<User> getFriendsByUser(Long id) {
        User user = findById(id).orElseThrow(() -> new NotFoundException("Пользователь с id = " + id + " не найден"));
        return user.getFriends().keySet()
                .stream()
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public Collection<User> getCommonFriends(Long id, Long otherId) {
        User user = findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + id + " не найден"));
        User otherUser = findById(otherId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + otherId + " не найден"));

        return user.getFriends().keySet()
                .stream()
                .filter(otherUser.getFriends().keySet()::contains)
                .map(this::findById)
                .filter(Optional::isPresent)
                .map(Optional::get)
                .toList();
    }

    @Override
    public void addFriend(Long id, Long friendId) {
        User user = findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + id + " не найден"));
        User friend = findById(friendId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + friendId + " не найден"));
        // добавляем в друзья пользователя
        Map<Long, Boolean> friends = user.getFriends();
        friends.put(friend.getId(), true);
        user.setFriends(friends);

        // добавляем пользователя в друзья у соответствующего друга
        Map<Long, Boolean> friendsOfFriend = friend.getFriends();
        friendsOfFriend.put(user.getId(), true);
        friend.setFriends(friendsOfFriend);
    }

    @Override
    public void deleteFriend(Long id, Long friendId) {
        User user = findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + id + " не найден"));
        User friend = findById(friendId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + friendId + " не найден"));

        // удаляем у пользователя
        Map<Long, Boolean> friends = user.getFriends();
        friends.remove(friend.getId());
        user.setFriends(friends);

        // удаляем из друзей пользователя у соответствующего друга
        Map<Long, Boolean> friendsOfFriend = friend.getFriends();
        friendsOfFriend.remove(user.getId());
        friend.setFriends(friendsOfFriend);

    }
}
