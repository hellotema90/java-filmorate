package ru.yandex.practicum.filmorate.controller;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
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
    @Autowired
    UserController userController;

    @BeforeEach
    public void beforeEach() {
        user1 = User.builder()
                .name("имя1")
                .login("логин1")
                .email("uemail1@test.ru")
                .birthday(LocalDate.of(2000, 1, 1))
                .build();
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
