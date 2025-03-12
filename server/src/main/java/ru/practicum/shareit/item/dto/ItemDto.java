package ru.practicum.shareit.item.dto;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.experimental.FieldDefaults;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.comments.dto.CommentDto;

import java.util.Collection;

@Builder
@Data
@NoArgsConstructor
@AllArgsConstructor
@FieldDefaults(level = AccessLevel.PRIVATE)
public class ItemDto {

    Long id;
    String name;
    String description;
    Boolean available;
    Long owner;
    Long requestId;
    Booking lastBooking;
    Booking nextBooking;
    Collection<CommentDto> comments;
}