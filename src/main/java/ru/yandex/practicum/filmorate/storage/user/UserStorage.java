package ru.yandex.practicum.filmorate.storage.user;

import ru.yandex.practicum.filmorate.model.User;
import java.util.Collection;
import java.util.Optional;

public interface UserStorage {

    Collection<User> getAllUsers();

    User createUser(User user);

    User updateUser(User updatedUser);

    Optional<User> findById(Long id);

    Collection<User> getFriendsByUser(Long id);

    Collection<User> getCommonFriends(Long id, Long otherId);

    void addFriend(Long id, Long friendId);

    void deleteFriend(Long id, Long friendId);

}