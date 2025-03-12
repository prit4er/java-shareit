package ru.practicum.shareit.booking.dto;

import com.fasterxml.jackson.annotation.JsonFormat;
import com.fasterxml.jackson.annotation.JsonProperty;
import jakarta.validation.constraints.Future;
import jakarta.validation.constraints.FutureOrPresent;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Positive;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.item.dto.ItemDto;
import ru.practicum.shareit.user.dto.UserDto;

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookItemRequestDto {

    Long id;

    @NotNull(message = "Start time cannot be null")
    @JsonProperty("start")
    LocalDateTime startTime;

    @NotNull(message = "End time cannot be null")
    @JsonProperty("end")
    LocalDateTime endTime;

    @NotNull(message = "Item ID cannot be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    Long itemId;

    BookingStatus status;
    UserDto booker;
    ItemDto item;
}