package ru.practicum.shareit.user;

import jakarta.validation.Valid;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;

@RestController
@RequestMapping(path = "/users")
public class UserController {

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @GetMapping
    public List<UserDto> getAllUsers() {
        List<User> users = userService.getAll();
        return UserDtoMapper.toUserDtoList(users);
    }

    @GetMapping("/{id}")
    public UserDto getUser(@PathVariable Integer id) {
        User user = userService.get(id);
        return UserDtoMapper.toUserDto(user);
    }

    @PostMapping
    public ResponseEntity<UserDto> addUser(@Valid @RequestBody UserDto userDto) {
        User user = UserDtoMapper.toUser(userDto);
        User savedUser = userService.add(user);
        return ResponseEntity.status(HttpStatus.CREATED).body(UserDtoMapper.toUserDto(savedUser));
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id) {
        User user = userService.get(id);
        if (user != null) {
            userService.delete(user);
        }
    }

    @PatchMapping("/{id}")
    public UserDto patchUser(@PathVariable Integer id, @RequestBody UserDto userDto) {
        User userToUpdate = UserDtoMapper.toUser(userDto);
        User updatedUser = userService.update(id, userToUpdate);
        return updatedUser != null ? UserDtoMapper.toUserDto(updatedUser) : null;
    }

}
