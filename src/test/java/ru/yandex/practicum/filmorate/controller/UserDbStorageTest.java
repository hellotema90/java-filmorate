package ru.yandex.practicum.filmorate.controller;

import lombok.RequiredArgsConstructor;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.context.SpringBootTest;
import ru.yandex.practicum.filmorate.dao.impl.UserDbStorage;
import ru.yandex.practicum.filmorate.exception.ResourceException;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;
import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@AutoConfigureTestDatabase
@RequiredArgsConstructor(onConstructor_ = @Autowired)
public class UserDbStorageTest {
    private final UserDbStorage userDbStorage;
    private User user1;
    private User user2;
    private User user3;

    @BeforeEach
    public void beforeEach() {
        user1 = User.builder()
                .name("имя1")
                .login("логин1")
                .email("email1@test.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
        userDbStorage.addUser(user1);
        user2 = User.builder()
                .name("имя2")
                .login("логин2")
                .email("email2@test.ru")
                .birthday(LocalDate.of(2000, 2, 2))
                .build();
        userDbStorage.addUser(user2);
        user3 = User.builder()
                .name("имя3")
                .login("логин3")
                .email("email3@test.ru")
                .birthday(LocalDate.of(2000, 3, 3))
                .build();
        userDbStorage.addUser(user3);
    }

        @AfterEach
        public void clearEach() {
            userDbStorage.deleteAllUsers();
        }

        @Test
        public void isFindUserById() {
            assertDoesNotThrow(() -> userDbStorage.getUserById(user1.getId()));
        }

        @Test
        void getUsersTest() {
            List<User> expectedList = new ArrayList<>();
            expectedList.add(user1);
            expectedList.add(user2);
            expectedList.add(user3);

            List<User> actualListUsers = userDbStorage.getAllUsers();

            assertEquals(expectedList, actualListUsers, "Списки пользователей не совпадают");
        }

        @Test
        void updateUserTest() {
            User expectedUpdateUser = User.builder()
                    .id(1)
                    .name("имя2")
                    .login("логин2")
                    .email("email2@test.ru")
                    .birthday(LocalDate.of(2000, 2, 2))
                    .build();
            userDbStorage.addUser(expectedUpdateUser);

            User actualUser = userDbStorage.updateUser(expectedUpdateUser);

            assertEquals(expectedUpdateUser, actualUser, "Пользователи не совпадают");
        }

        @Test
        public void addFriendsTest() {
            userDbStorage.addToFriends(user1.getId(), user2.getId());
            List<User> actual = userDbStorage.getFriendsById(user1.getId());

            assertEquals(1, actual.size(), "Списки друзей не совпадают");
        }

        @Test
        void getListGeneralFriendsTest() {
            userDbStorage.addToFriends(user1.getId(), user2.getId());
            userDbStorage.addToFriends(user3.getId(), user2.getId());
            List<User> actual = userDbStorage.getGeneralListFriends(user1.getId(), user3.getId());

            assertEquals(1, actual.size(), "Списки друзей не совпадают");
        }

        @Test
        public void deleteUserByIdTest() {
            userDbStorage.deleteUserById(user1.getId());

            assertThrows(ResourceException.class, () -> {
                userDbStorage.getUserById(user1.getId());
            });
        }

        @Test
        void deleteUsersTest() {
            List<User> expectedList = new ArrayList<>();

            userDbStorage.deleteAllUsers();
            List<User> actualListUsers = userDbStorage.getAllUsers();

            assertEquals(expectedList, actualListUsers, "Список не пустой");
        }


    }
