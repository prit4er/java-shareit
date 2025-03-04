package ru.practicum.shareit.models.user.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UserDto {

    private Integer id;

    @NotBlank(message = "Not able to add user with blank name")
    private String name;

    @NotBlank(message = "Not able to add user with blank email")
    @Size(max = 255,
            message = "Maximum email address size exceeded (greater than 255)")
    @Email()
    private String email;
}