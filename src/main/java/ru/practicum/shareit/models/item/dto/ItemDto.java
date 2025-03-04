package ru.practicum.shareit.models.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ItemDto {

    private Integer id;

    @NotBlank(message = "Not able to add item with blank name")
    private String name;

    @NotBlank
    private String description;

    @NotNull(message = "Not able to add item if available-status is NULL")
    private Boolean available;

    private Integer owner;
    private LocalDateTime start;
    private LocalDateTime end;
}
