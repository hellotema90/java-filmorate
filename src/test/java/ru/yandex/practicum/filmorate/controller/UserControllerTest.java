package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import java.time.LocalDate;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private User user1;
    private UserController userController;
    @BeforeEach
    public void beforeEach(){
        userController = new UserController();
        user1 = new User(1, "email1@test.ru", "логин1", "имя1",
                LocalDate.of(2000, 1, 1));
    }

    @Test
    void createUser() {
        userController.create(user1);
        assertEquals(1, userController.findAllUsers().size());
        User user2 = new User(2, "email2@test.ru", "логин2", "имя2",
                LocalDate.of(2000, 1, 1));
        userController.create(user2);
        assertEquals(2, userController.findAllUsers().size());
    }

    @Test
    void putUser() {
        userController.create(user1);
        assertEquals(1, userController.findAllUsers().size());
        assertEquals(user1.getEmail(), "email1@test.ru");
        user1.setEmail("email2@test.ru");
        userController.put(user1);
        assertEquals(user1.getEmail(), "email2@test.ru");
    }
}