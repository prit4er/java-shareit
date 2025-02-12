package ru.practicum.shareit.user;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;

@Builder
@Getter
@Setter
@EqualsAndHashCode
public class User {

    private Integer id;
    private String email;
    private String name;
}
