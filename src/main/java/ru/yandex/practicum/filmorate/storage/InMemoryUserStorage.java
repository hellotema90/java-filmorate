package ru.yandex.practicum.filmorate.storage;

import org.springframework.stereotype.Component;
import ru.yandex.practicum.filmorate.model.User;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.concurrent.atomic.AtomicInteger;

@Component
public class InMemoryUserStorage implements UserStorage {
    private final Map<Integer, User> users = new HashMap<>();
    private final AtomicInteger generatorId = new AtomicInteger(0);

    private int countGeneratorId() {
        return generatorId.incrementAndGet();
    }

    @Override
    public User addUser(User user) {
        user.setId(countGeneratorId());
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public User updateUser(User user) {
        users.put(user.getId(), user);
        return user;
    }

    @Override
    public List<User> getAllUsers() {
        return new ArrayList<>(users.values());
    }

    @Override
    public User getUserById(int id) {
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
        users.get(idFriend).getFriends().remove(id);
    }

    public void deleteUserById(int id) {

    }

    public void deleteAllUsers() {

    }

    public List<User> getGeneralListFriends(int idUser1, int idUser2) {
        return null;
    }

    public List<User> getFriendsById(int id) {
        return null;
    }
}
