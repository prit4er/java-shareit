package ru.practicum.shareit.user;

import org.springframework.stereotype.Repository;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.Optional;

@Repository
public class UserRepositoryImp implements UserRepository {

    private final Map<Integer, User> userRepository = new HashMap<>();
    private Integer idCounter = 0;

    @Override
    public List<User> findAll() {
        return new ArrayList<>(userRepository.values());
    }

    @Override
    public User save(User user) {
        if (user.getId() == null) {
            user.setId(generateId());
        }
        userRepository.put(user.getId(), user);
        return user;
    }

    @Override
    public void deleteById(Integer id) {
        userRepository.remove(id);
    }

    @Override
    public User findById(Integer id) {
        return userRepository.get(id);
    }

    @Override
    public Optional<User> findByEmail(String email) {
        return userRepository.values().stream()
                             .filter(user -> email.equals(user.getEmail()))
                             .findFirst();
    }

    @Override
    public boolean existsById(Integer id) {
        return userRepository.containsKey(id);
    }

    private Integer generateId() {
        return ++idCounter;
    }
}