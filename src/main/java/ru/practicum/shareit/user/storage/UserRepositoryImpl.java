package ru.practicum.shareit.user.storage;

import org.springframework.stereotype.Repository;
import ru.practicum.shareit.user.model.User;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Repository
public class UserRepositoryImpl {
    private final List<User> users = new ArrayList<User>();
    private static Long idUser = 0L;

    public User saveUser(User user) {
        user.setId(idUser);
        users.add(user);
        idUser++;
        return user;
    }

    public Optional<User> findUserById(Long userId) {
        return users.stream()
                .filter(user -> user.getId().equals(userId))
                .findFirst();
    }

    public void delete(Long userId) {
        users.removeIf(user -> user.getId().equals(userId));
    }

    public Optional<User> findUserByEmail(String email) {
        return users.stream()
                .filter(user -> user.getEmail().equals(email))
                .findFirst();
    }
}
