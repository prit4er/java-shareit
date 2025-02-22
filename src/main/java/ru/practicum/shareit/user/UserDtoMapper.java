package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

public class UserDtoMapper {

    // Маппинг User -> UserDto
    public static UserDto toDto(User user) {
        if (user == null) {
            return null;
        }
        return UserDto.builder()
                      .id(user.getId())
                      .email(user.getEmail())
                      .name(user.getName())
                      .build();
    }

    // Маппинг UserDto -> User
    public static User toEntity(UserDto userDto) {
        if (userDto == null) {
            return null;
        }
        return User.builder()
                   .id(userDto.getId())
                   .email(userDto.getEmail())
                   .name(userDto.getName())
                   .build();
    }


    // Маппинг коллекции User -> List<UserDto>
    public static List<UserDto> toDto(List<User> users) {
        if (users == null || users.isEmpty()) {
            return List.of();
        }
        return users.stream()
                    .map(UserDtoMapper::toDto)
                    .collect(Collectors.toList());
    }

    // Маппинг коллекции UserDto -> List<User>
    public static List<User> toEntity(List<UserDto> userDtos) {
        if (userDtos == null || userDtos.isEmpty()) {
            return List.of();
        }
        return userDtos.stream()
                       .map(UserDtoMapper::toEntity)
                       .collect(Collectors.toList());
    }
}
