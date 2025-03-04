package ru.practicum.shareit.models.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.exeptions.ConflictException;
import ru.practicum.shareit.exeptions.NotFoundException;
import ru.practicum.shareit.models.user.dto.UserDto;

import java.util.List;

@Service
@RequiredArgsConstructor
@Transactional
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public UserDto add(UserDto userDto) {
        User user = UserDtoMapper.toEntity(userDto);
        if (isEmailTaken(user.getEmail())) {
            throw new ConflictException("Email is already taken");
        }
        return UserDtoMapper.toDto(userRepository.save(user));
    }

    @Override
    public void delete(Integer id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new NotFoundException("User not found with ID " + id));
        userRepository.delete(user);
    }

    @Override
    public List<UserDto> getAll() {
        List<User> users = userRepository.findAll();
        return UserDtoMapper.toDto(users);
    }

    @Override
    public UserDto getById(Integer id) {
        User user = userRepository.findById(id)
                                  .orElseThrow(() -> new NotFoundException("User not found with ID " + id));
        return UserDtoMapper.toDto(user);
    }

    @Override
    public UserDto update(Integer id, UserDto updatedUserDto) {
        User existingUser = userRepository.findById(id)
                                          .orElseThrow(() -> new NotFoundException("User not found with ID " + id));

        if (updatedUserDto.getEmail() != null && !updatedUserDto.getEmail().equals(existingUser.getEmail())) {
            if (isEmailTaken(updatedUserDto.getEmail())) {
                throw new ConflictException("Email is already taken");
            }
            existingUser.setEmail(updatedUserDto.getEmail());
        }
        if (updatedUserDto.getName() != null) {
            existingUser.setName(updatedUserDto.getName());
        }

        return UserDtoMapper.toDto(userRepository.save(existingUser));
    }

    private boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}