package ru.yandex.practicum.filmorate.service;

import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.exception.NotFoundException;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.user.UserStorage;

import java.util.*;

@Slf4j
@Service
public class UserService {

    private final UserStorage userStorage;

    @Autowired
    public UserService(UserStorage userStorage) {
        this.userStorage = userStorage;
    }

    public Collection<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User createUser(User user) {
        return userStorage.createUser(user);
    }

    public User updateUser(User updatedUser) {
        return userStorage.updateUser(updatedUser);
    }

    public User findUserById(Integer id) {
        log.debug("Вызван метод findUserById id = {}", id);
        return userStorage.getAllUsers().stream()
                .filter(user -> user.getId().equals(id))
                .findFirst()
                .orElseThrow(() -> new NotFoundException(String.format("Пользователь с id %d не найден", id)));
    }

    public void addFriend(Integer id, Integer friendId) {
        log.debug("Вызван метод addFriend id = {}, friendId = {}", id, friendId);
        User user = findUserById(id);
        User friend = findUserById(friendId);

        // добавляем в друзья пользователя
        Set<Integer> friends = user.getFriends();
        friends.add(friend.getId());
        user.setFriends(friends);

        // добавляем пользователя в друзья у соответствующего друга
        Set<Integer> friendsOfFriend = friend.getFriends();
        friendsOfFriend.add(user.getId());
        friend.setFriends(friendsOfFriend);
    }

    public void deleteFriend(Integer id, Integer friendId) {
        log.debug("Вызван метод deleteFriend id = {}, friendId = {}", id, friendId);
        User user = findUserById(id);
        User friend = findUserById(friendId);

        // удаляем у пользователя
        Set<Integer> friends = user.getFriends();
        friends.remove(friend.getId());
        user.setFriends(friends);

        // удаляем из друзей пользователя у соответствующего друга
        Set<Integer> friendsOfFriend = friend.getFriends();
        friendsOfFriend.remove(user.getId());
        friend.setFriends(friendsOfFriend);
    }

    public Collection<User> getFriendsByUser(Integer id) {
        log.debug("Вызван метод getFriendsByUser id = {}", id);
        User user = findUserById(id);
        return user.getFriends()
                .stream()
                .map(this::findUserById)
                .toList();
    }

    public Collection<User> getCommonFriends(Integer id, Integer otherId) {
        log.debug("Вызван метод getCommonFriends id = {}, otherId = {}", id, otherId);
        User user = findUserById(id);
        User otherUser = findUserById(otherId);
        return user.getFriends()
                .stream()
                .filter(otherUser.getFriends()::contains)
                .map(this::findUserById)
                .toList();
    }
}