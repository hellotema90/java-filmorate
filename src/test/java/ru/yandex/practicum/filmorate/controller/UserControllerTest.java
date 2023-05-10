/*
package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.ConstraintViolation;
import javax.validation.Validation;
import javax.validation.Validator;
import java.time.LocalDate;
import java.util.Set;

import static org.junit.jupiter.api.Assertions.*;

class UserControllerTest {
    private final Validator validator = Validation.buildDefaultValidatorFactory().getValidator();
    private User user1;
    private UserController userController;

    @BeforeEach
    public void beforeEach() {
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

    @Test
    void createUserWithNoSymbolAt() {
        user1.setEmail("email.com");
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertEquals(1, violations.size(), "Валидация не корректна, почта должна содержать символ @");
    }

    @Test
    void createUserWithBlankLogin() {
        user1.setLogin("");
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertEquals(1, violations.size(), "Валидация не корректна, логин не может быть" +
                " пустым и содержать пробелы");
    }

    @Test
    void createUserWithWrongDateBirthday() {
        user1.setBirthday(LocalDate.of(2050, 1, 1));
        Set<ConstraintViolation<User>> violations = validator.validate(user1);
        assertEquals(1, violations.size(), "Валидация не корректна, дата рождения не может" +
                " быть в будущем");
    }
}

 */