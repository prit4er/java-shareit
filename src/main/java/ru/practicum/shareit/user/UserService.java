package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    User add(User user);

    void delete(Integer id);

    List<User> getAll();

    User get(Integer id);

    User update(Integer id, User updatedUser);

}
