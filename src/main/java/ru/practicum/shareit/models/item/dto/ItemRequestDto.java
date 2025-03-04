package ru.practicum.shareit.models.item.dto;

import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import ru.practicum.shareit.models.booking.dto.BookingDto;
import ru.practicum.shareit.models.comments.dto.CommentDto;

import java.util.List;

@Builder
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class ItemRequestDto {

    private Integer id;

    @NotBlank(message = "Название должно быть указано!")
    private String name;

    @NotBlank(message = "Описание должно быть указано!")
    private String description;

    private Integer owner;

    private BookingDto nextBooking;

    private BookingDto lastBooking;

    private List<CommentDto> comments;

    @NotNull(message = "Статус аренды должен быть указан!")
    private Boolean available;
}
