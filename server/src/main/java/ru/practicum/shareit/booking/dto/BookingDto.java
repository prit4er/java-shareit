package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookingDto {

    Long id;

    @NotNull(message = "Start time cannot be null")
    LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    LocalDateTime endTime;

    @NotNull(message = "Item ID cannot be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Long itemId;

    BookingStatus status;

    UserDto booker;

    ItemDto item;
}