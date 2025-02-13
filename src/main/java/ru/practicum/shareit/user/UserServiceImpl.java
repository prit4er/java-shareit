package ru.practicum.shareit.user;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import ru.practicum.shareit.exeptions.ConflictException;

import java.util.List;

@Service
@RequiredArgsConstructor
public class UserServiceImpl implements UserService {

    private final UserRepository userRepository;

    @Override
    public User add(User user) {
        if (isEmailTaken(user.getEmail())) {
            throw new ConflictException("Email is already taken");
        }
        return userRepository.save(user);
    }

    @Override
    public void delete(Integer id) {
        User user = get(id); // Проверяем существование, выбрасываем NotFoundException при отсутствии
        userRepository.deleteById(id); // Удаляем пользователя по ID
    }

    @Override
    public List<User> getAll() {
        return userRepository.findAll();
    }

    @Override
    public User get(Integer id) {
        User user = userRepository.findById(id);
        if (user == null) {
            throw new IllegalArgumentException("User not found");
        }
        return user;
    }

    @Override
    public User update(Integer id, User updatedUser) {
        User existingUser = userRepository.findById(id);
        if (existingUser == null) {
            throw new IllegalArgumentException("User not found");
        }

        if (updatedUser.getEmail() != null && !updatedUser.getEmail().equals(existingUser.getEmail())) {
            if (isEmailTaken(updatedUser.getEmail())) {
                throw new ConflictException("Email is already taken");
            }
            existingUser.setEmail(updatedUser.getEmail());
        }
        if (updatedUser.getName() != null) {
            existingUser.setName(updatedUser.getName());
        }
        return userRepository.save(existingUser);
    }

    private boolean isEmailTaken(String email) {
        return userRepository.findByEmail(email).isPresent();
    }
}
