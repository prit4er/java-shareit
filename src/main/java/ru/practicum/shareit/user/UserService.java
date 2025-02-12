package ru.practicum.shareit.user;

import java.util.List;

public interface UserService {

    User add(User user);

    User delete(User user);

    List<User> getAll();

    User get(Integer id);

    User update(Integer id, User updatedUser);

}
