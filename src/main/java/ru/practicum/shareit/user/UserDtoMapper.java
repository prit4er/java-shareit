package ru.practicum.shareit.user;

import ru.practicum.shareit.user.dto.UserDto;

import java.util.List;
import java.util.stream.Collectors;

public class UserDtoMapper {

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


    public static List<UserDto> toDto(List<User> users) {
        if (users == null || users.isEmpty()) {
            return List.of();
        }
        return users.stream()
                    .map(UserDtoMapper::toDto)
                    .collect(Collectors.toList());
    }

    public static List<User> toEntity(List<UserDto> userDto) {
        if (userDto == null || userDto.isEmpty()) {
            return List.of();
        }
        return userDto.stream()
                       .map(UserDtoMapper::toEntity)
                       .collect(Collectors.toList());
    }
}
