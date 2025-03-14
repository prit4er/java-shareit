package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.NotFoundException;
import ru.practicum.shareit.user.dto.UserDto;

import java.util.Collection;

@Slf4j
@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {
    private final UserRepository userRepository;

    public Collection<UserDto> getUsers() {
        return userRepository.findAll().stream()
                             .map(UserDtoMapper::toDto)
                             .toList();
    }

    public UserDto getUserById(Long userId) {
        User user = fetchUserById(userId);

        return UserDtoMapper.toDto(user);
    }

    public UserDto createUser(UserDto userDto) {
        validateEmail(userDto.getEmail());
        log.info("Creating user: {}", userDto);
        User user = UserDtoMapper.toEntity(userDto);
        User createdUser = userRepository.save(user);
        log.info("User saved in DB: id={}, name={}, email={}",
                 createdUser.getUserId(), createdUser.getName(), createdUser.getEmail());
        UserDto result = UserDtoMapper.toDto(createdUser);
        log.info("Returning UserDto: {}", result);
        return result;
    }

    public UserDto updateUser(Long userId, UserDto userDto) {
        log.info("Starting updateUser with id: {}", userId);
        User existingUser = fetchUserById(userId);

        if (userDto.getEmail() != null &&
                (existingUser.getEmail() == null || !existingUser.getEmail().equals(userDto.getEmail()))) {
            log.info("Validating email: {}", userDto.getEmail());
            validateEmail(userDto.getEmail());
        }

        log.info("Updating user fields");
        UserDtoMapper.updateUserFields(existingUser, userDto);

        User updatedUser = userRepository.save(existingUser);

        log.info("User updated successfully: id = {}", updatedUser.getUserId());
        return UserDtoMapper.toDto(updatedUser);
    }

    public void deleteUser(Long userId) {
        if (!userRepository.existsById(userId)) {
            throw new NotFoundException(String.format("User with id %s not found", userId));
        }
        userRepository.deleteById(userId);
        log.info("User deleted successfully: id = {}", userId);
    }

    private void validateEmail(String email) {
        if (email == null || email.isEmpty()) {
            throw new IllegalArgumentException("Email must be provided");
        }
        if (userRepository.existsByEmail(email)) {
            throw new IllegalArgumentException("Email already in use by another user");
        }
    }

    private User fetchUserById(Long userId) {
        if (userId == null) {
            log.error("User id must not be null.");
            throw new IllegalArgumentException("User id must not be null.");
        }

        User fetchedUser = userRepository.findById(userId)
                                         .orElseThrow(() -> {
                                             log.error("User with id {} not found", userId);
                                             return new NotFoundException(String.format("User with id %s not found", userId));
                                         });

        log.info("User fetched successfully: id = {}, name = {}, email = {}",
                 fetchedUser.getUserId(), fetchedUser.getName(), fetchedUser.getEmail());

        return fetchedUser;
    }
}