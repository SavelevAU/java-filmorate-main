package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Qualifier;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.*;
import ru.yandex.practicum.filmorate.mapper.UserMapper;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.dto.*;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;


    @Autowired
    public UserService(@Qualifier("userDbStorage") UserStorage userStorage) {
        this.userStorage = userStorage;
    }


    public Collection<UserDto> getAllUsers() {
            return userStorage.getAllUsers()
                    .stream()
                    .map(UserMapper::mapToUserDto)
                    .toList();
        }

        public UserDto createUser(NewUserRequest request) {
            if (request.getName() == null || request.getName().isBlank()) {
                request.setName(request.getLogin());
            }
            User user = userStorage.createUser(UserMapper.mapToUser(request));
            return UserMapper.mapToUserDto(user);
        }

        public UserDto updateUser(UpdateUserRequest request) {
            if (request.getId() == null) {
                log.warn("Ошибка при обновлении фильма {}: Id должен быть указан", request);
                throw new ValidationException("Id должен быть указан");
            }
            User user = userStorage.findById(request.getId()).orElseThrow(()
                    -> new NotFoundException(String.format("Пользователь с id %d не найден",
                    request.getId())));
            user = UserMapper.updateUserFields(user, request);
            user = userStorage.updateUser(user);
            return UserMapper.mapToUserDto(user);
        }

    public UserDto findUserById(Long id) {
        log.debug("Вызван метод findUserById id = {}", id);
        return userStorage.findById(id)
                .map(UserMapper::mapToUserDto)
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", id)));
    }

    public void addFriend(Long id, Long friendId) {
        log.debug("Вызван метод addFriend id = {}, friendId = {}", id, friendId);
        User user = userStorage.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + id + " не найден"));
        User friend = userStorage.findById(friendId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + friendId + " не найден"));
        userStorage.addFriend(id, friendId);
    }

    public void deleteFriend(Long id, Long friendId) {
        log.debug("Вызван метод deleteFriend id = {}, friendId = {}", id, friendId);
        User user = userStorage.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + id + " не найден"));
        User friend = userStorage.findById(friendId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + friendId + " не найден"));
        userStorage.deleteFriend(id, friendId);
    }

    public Collection<UserDto> getFriendsByUser(Long id) {
        log.debug("Вызван метод getFriendsByUser id = {}", id);
        User user = userStorage.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + id + " не найден"));
        Collection<User> friends = userStorage.getFriendsByUser(id);
        return friends.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }

    public Collection<UserDto> getCommonFriends(Long id, Long otherId) {
        log.debug("Вызван метод getCommonFriends id = {}, otherId = {}", id, otherId);
        User user = userStorage.findById(id).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + id + " не найден"));
        User friend = userStorage.findById(otherId).orElseThrow(() ->
                new NotFoundException("Пользователь с id = " + otherId + " не найден"));
        Collection<User> commonFriends = userStorage.getCommonFriends(id, otherId);
        return commonFriends.stream()
                .map(UserMapper::mapToUserDto)
                .toList();
    }
}