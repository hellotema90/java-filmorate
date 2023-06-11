package ru.yandex.practicum.filmorate.service;

import org.springframework.beans.factory.annotation.Qualifier;
import ru.yandex.practicum.filmorate.exception.ResourceException;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import ru.yandex.practicum.filmorate.model.User;
import ru.yandex.practicum.filmorate.storage.UserStorage;

import java.util.List;

@Slf4j
@Service
public class UserService {
    @Autowired
    @Qualifier("userDao")
    UserStorage userStorage;

    public User addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        userStorage.addUser(user);
        log.debug("Пользователь успешно добавлен.");
        return user;
    }

    public User updateUser(User user) {
        validateContainsId(user.getId());
        userStorage.updateUser(user);
        log.debug("Информация о пользователе успешно обновлена.");
        return user;
    }

    public List<User> getAllUsers() {
        return userStorage.getAllUsers();
    }

    public User getUserById(int id) {
        validateContainsId(id);
        return userStorage.getUserById(id);
    }

    public void addFriend(int idUser, int friendId) {
        validateContainsId(friendId);
        userStorage.addToFriends(idUser, friendId);
        log.debug("Теперь вы в списке друзей.");
    }

    public void deleteFriend(int idUSer, int friendId) {
        userStorage.deleteFromFriends(idUSer, friendId);
        log.debug("Удален из списка друзей.");
    }

    public List<User> getFriendsById(int id) {
        return userStorage.getFriendsById(id);
    }

    public List<User> getListFriends(int id, int otherId) {
        return userStorage.getGeneralListFriends(id, otherId);
    }

    public void deleteUserById(int id) {
        userStorage.deleteUserById(id);
    }

    public void deleteAllUsers() {
        userStorage.deleteAllUsers();
    }

    public void addToFriends(int idUser, int idFriend) {
        userStorage.addToFriends(idUser, idFriend);
    }

    private void validateContainsId(int id) {
        if (userStorage.getUserById(id) == null) {
            throw new ResourceException(HttpStatus.NOT_FOUND, "Пользователь с таким id не найден.");
        }
    }
}

