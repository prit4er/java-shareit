package ru.practicum.shareit.models.booking.dto;


import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.models.booking.BookingStatus;
import ru.practicum.shareit.models.item.dto.ItemDto;
import ru.practicum.shareit.models.user.dto.UserDto;

import java.time.LocalDateTime;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class BookingResponseDto {

    private Integer id;

    private LocalDateTime start;

    private LocalDateTime end;

    private ItemDto item;

    private UserDto booker;

    private BookingStatus status;
}
