package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private int generatorId = 0;

    // счетчик для выдачи уникального id
    private int countGeneratorId() {
        return ++generatorId;
    }

    @Override
    public void addUser(User user) {
        if (user.getName() == null || user.getName().isBlank()) {
            user.setName(user.getLogin());
        }
        user.setId(countGeneratorId());
        users.put(user.getId(), user);
    }

    @Override
    public void updateUser(User user) {
        users.put(user.getId(), user);
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserByID(int id) {
        return users.get(id);
    }

    @Override
    public void addToFriends(int id, int idFriend) {
        users.get(id).getFriends().add(idFriend);
        users.get(idFriend).getFriends().add(id);
    }

    @Override
    public void deleteFromFriends(int id, int idFriend) {
        users.get(id).getFriends().remove(idFriend);
        users.get(idFriend).getFriends().remove(idFriend);
    }
}
