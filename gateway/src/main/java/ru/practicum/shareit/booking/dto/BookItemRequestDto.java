package ru.practicum.shareit.booking.dto;

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

import java.time.LocalDateTime;

@Getter
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class BookItemRequestDto {

    @Positive
    @NotNull(message = "Item ID cannot be null")
    @JsonProperty(access = JsonProperty.Access.WRITE_ONLY)
    long itemId;

    @NotNull(message = "Start time cannot be null")
    @FutureOrPresent(message = "Start time must be in the present or future")
    @JsonProperty("start")
    LocalDateTime start;

    @Future(message = "End time must be in the future")
    @NotNull(message = "End time cannot be null")
    @JsonProperty("end")
    LocalDateTime end;
}