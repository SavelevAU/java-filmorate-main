package ru.yandex.practicum.filmorate;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.jdbc.JdbcTest;
import org.springframework.jdbc.core.JdbcTemplate;
import org.springframework.context.annotation.Import;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.mappers.UserRowMapper;
import ru.yandex.practicum.filmorate.storage.user.UserDbStorage;

import java.time.LocalDate;
import java.util.Collection;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@JdbcTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
@Import({UserDbStorage.class, UserRowMapper.class})
class UserApplicationTests {

    private final UserDbStorage userStorage;
    private final JdbcTemplate jdbcTemplate;

    private User user;
    private User user2;
    private User user3;

    @BeforeEach
    void setUp() {

        jdbcTemplate.update("DELETE FROM user_friends");
        jdbcTemplate.update("DELETE FROM users");
        jdbcTemplate.update("DELETE FROM likes");
        jdbcTemplate.update("ALTER TABLE users ALTER COLUMN id RESTART WITH 1");
        jdbcTemplate.update("DELETE FROM films");
        jdbcTemplate.update("ALTER TABLE films ALTER COLUMN id RESTART WITH 1");

        user = new User();
        user.setEmail("test@mail.ru");
        user.setLogin("login");
        user.setName("name");
        user.setBirthday(LocalDate.now());
        userStorage.createUser(user);

        user2 = new User();
        user2.setEmail("test2@mail.ru");
        user2.setLogin("login2");
        user2.setName("name2");
        user2.setBirthday(LocalDate.now());
        userStorage.createUser(user2);

        user3 = new User();
        user3.setEmail("test3@mail.ru");
        user3.setLogin("login3");
        user3.setName("name3");
        user3.setBirthday(LocalDate.now());
        userStorage.createUser(user3);

    }

    @Test
    public void testFindUserById() {

        Optional<User> userOptional = userStorage.findById(1L);

        assertThat(userOptional)
                .isPresent()
                .hasValueSatisfying(user ->
                        assertThat(user).hasFieldOrPropertyWithValue("id", 1L)
                );
    }

    @Test
    public void testFindAllUsers() {

        Collection<User> allUsers = userStorage.getAllUsers();

        assertThat(allUsers.size()).isEqualTo(3);

        // Проверяем наличие всех email
        List<String> emails = allUsers.stream()
                .map(User::getEmail)
                .toList();
        assertThat(emails.contains("test@mail.ru")).isTrue();
    }

    @Test
    public void testUpdateUser() {

        user.setName("Updated name");
        userStorage.updateUser(user);

        Collection<User> allUsers = userStorage.getAllUsers();

        assertThat(allUsers.size()).isEqualTo(3);

        // Проверяем наличие всех email
        List<String> names = allUsers.stream()
                .map(User::getName)
                .toList();
        assertThat(names.contains("Updated name")).isTrue();
    }

    @Test
    public void testAddFriend() {
        userStorage.addFriend(1L, 2L);
        Collection<User> friends = userStorage.getFriendsByUser(1L);
        assertThat(friends.size()).isEqualTo(1);

        List<Long> ids = friends.stream()
                .map(User::getId)
                .toList();
        assertThat(ids.contains(2L)).isTrue();

        Collection<User> user2Friends = userStorage.getFriendsByUser(2L);
        assertThat(user2Friends.isEmpty()).isTrue();
    }

    @Test
    public void testDeleteFriend() {
        userStorage.addFriend(1L, 2L);
        Collection<User> friends = userStorage.getFriendsByUser(1L);
        assertThat(friends.size()).isEqualTo(1);

        List<Long> ids = friends.stream()
                .map(User::getId)
                .toList();
        assertThat(ids.contains(2L)).isTrue();

        Collection<User> user2Friends = userStorage.getFriendsByUser(2L);
        assertThat(user2Friends.isEmpty()).isTrue();
    }

    @Test
    public void testGetCommonFriends() {
        Collection<User> commonFriendsBefore = userStorage.getCommonFriends(2L, 3L);
        assertThat(commonFriendsBefore.isEmpty()).isTrue();

        userStorage.addFriend(2L, 1L);
        userStorage.addFriend(3L, 1L);

        Collection<User> commonFriendsAfter = userStorage.getCommonFriends(2L, 3L);
        assertThat(commonFriendsAfter.size()).isEqualTo(1);
        List<Long> ids = commonFriendsAfter.stream()
                .map(User::getId)
                .toList();
        assertThat(ids.contains(1L)).isTrue();
    }
}