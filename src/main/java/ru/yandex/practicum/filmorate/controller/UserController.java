package ru.yandex.practicum.filmorate.controller;

import lombok.extern.slf4j.Slf4j;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import ru.yandex.practicum.filmorate.model.User;

import javax.validation.Valid;

import java.util.*;

@Validated
@RestController
@RequestMapping("/users")
@Slf4j
public class UserController {
    private final Map<Integer, User> users = new HashMap<>();
    private int generatorId = 0;

    // счетчик для выдачи уникального id
    private int countGeneratorId() {
        return ++generatorId;
    }

    @GetMapping
    public Collection<User> findAllUsers() {
        return users.values();
    }

    @PostMapping
    public User create(@Valid @RequestBody User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        users.put(user.setId(countGeneratorId()), user);
        log.info("Добавлен пользователь {}", user);
        return user;
    }

    @PutMapping
    public User put(@Valid @RequestBody User user) {
        if (users.get(user.getId()).getId() == user.getId()) {
            users.replace(user.getId(), user);
            log.info("Изменен пользователь {}", user);
        }
        return user;
    }
}
