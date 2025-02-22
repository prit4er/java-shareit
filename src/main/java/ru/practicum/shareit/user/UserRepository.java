package ru.practicum.shareit.user;

import java.util.List;
import java.util.Optional;

public interface UserRepository {

    List<User> findAll();

    User save(User user);

    void deleteById(Integer id);

    User findById(Integer id);

    Optional<User> findByEmail(String email);

    boolean existsById(Integer id);
}
