package ru.practicum.shareit.models.user;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.models.user.dto.UserDto;

import java.util.List;

@Transactional(readOnly = true)
public interface UserService {

    @Transactional
    UserDto add(UserDto user);

    @Transactional
    void delete(Integer id);

    List<UserDto> getAll();

    UserDto getById(Integer id);

    @Transactional
    UserDto update(Integer id, UserDto updatedUser);

}
