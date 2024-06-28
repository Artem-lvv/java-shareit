package ru.practicum.shareit.user.repository;

import org.springframework.stereotype.Repository;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.user.model.User;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;

@Repository
@Transactional
public class UserRepository {
    private Long currentMaxId = 0L;
    private final Map<Long, User> idToUser = new HashMap<>();

    public User save(User user) {
        Long id = getNextId();

        user.setId(id);
        idToUser.put(id, user);

        return user;
    }

    public User update(User user) {
        idToUser.put(user.getId(), user);
        return user;
    }

    public void deleteById(Long id) {
        idToUser.remove(id);
    }

    public Optional<User> findById(Long id) {
        return Optional.ofNullable(idToUser.get(id));
    }

    public List<User> findAll() {
        return idToUser.values()
                .stream()
                .toList();
    }

    public Optional<User> findByEmail(String email) {
        return idToUser.values()
                .stream()
                .filter(user -> user.getEmail().equals(email))
                .map(Optional::ofNullable)
                .findFirst()
                .flatMap(Function.identity());
    }

    private long getNextId() {
        return ++currentMaxId;
    }
}
