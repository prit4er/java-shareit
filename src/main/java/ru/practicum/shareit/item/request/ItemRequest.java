package ru.practicum.shareit.item.request;

import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.Setter;
import lombok.ToString;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.comments.dto.CommentDto;

import java.util.List;

@Builder
@Getter
@Setter
@ToString
@EqualsAndHashCode
public class ItemRequest {

    private Integer id;

    private String name;

    private String description;

    private Boolean available;

    private Integer owner;

    private BookingDto nextBooking;

    private BookingDto lastBooking;

    private List<CommentDto> comments;
}
