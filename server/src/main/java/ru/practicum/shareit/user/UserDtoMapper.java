package ru.practicum.shareit.user;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.user.dto.UserDto;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public final class UserDtoMapper {

    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                      .id(user.getUserId())
                      .name(user.getName())
                      .email(user.getEmail())
                      .build();
    }

    public static User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return User.builder()
                   .userId(userDto.getId())
                   .name(userDto.getName())
                   .email(userDto.getEmail())
                   .build();
    }

    public static void updateUserFields(User user, UserDto userDto) {
        if (user == null || userDto == null) {
            return;
        }
        if (userDto.getName() != null) {
            user.setName(userDto.getName());
        }
        if (userDto.getEmail() != null) {
            user.setEmail(userDto.getEmail());
        }
    }
}
