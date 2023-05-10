package ru.yandex.practicum.filmorate.storage;

import ru.yandex.practicum.filmorate.model.User;

import java.util.List;

public interface UserStorage {
    void addUser(User user);

    void updateUser(User user);

    List<User> getAllUsers();

    User getUserByID(int id);

    void addToFriends(int id, int idUser);

    void deleteFromFriends(int id, int idUser);
}
