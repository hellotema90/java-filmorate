package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.exception.ValidationException;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

import java.time.LocalDate;
import java.util.*;

@Validated
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int generatorId = 0;

    //счетчик выдачи уникального id
    private int countGeneratorId() {
        return ++generatorId;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (validateUser(user)) {
            users.put(user.setId(countGeneratorId()), user);
            log.info("Добавлен пользователь {}", user);
        }
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if (users.get(user.getId()).getId() == user.getId()) {
            if (validateUser(user)) {
                users.replace(user.setId(user.getId()), user);
                log.info("Изменен пользователь {}", user);
            }
        }
        return user;
    }

    private static boolean validateUser(User user) {
        if (user.getEmail() == null || user.getEmail().isEmpty() || !user.getEmail().contains("@")) {
            log.warn("невалидная почта");
            throw new ValidationException("электронная почта не может быть пустой и должна содержать символ @");
        }
        if (user.getLogin().contains(" ")) {
            log.warn("логин не может содержать пробелы");
            throw new ValidationException("логин не может содержать пробелы");
        }
        if (user.getName() == null || user.getName().isBlank()) {
            log.warn("пустое имя");
            user.setName(user.getLogin());
        }
        if (user.getBirthday().isAfter(LocalDate.now())) {
            log.warn("указана неверная дата рождения");
            throw new ValidationException("указана неверная дата рождения");
        }
        return true;
    }
}
