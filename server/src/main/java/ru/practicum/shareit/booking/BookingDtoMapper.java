package ru.practicum.shareit.booking;

import lombok.AccessLevel;
import lombok.NoArgsConstructor;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.item.ItemDtoMapper;
import ru.practicum.shareit.user.User;
import ru.practicum.shareit.user.UserDtoMapper;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class BookingDtoMapper {

    public static BookingDto toDto(Booking booking) {
        return new BookingDto(
                booking.getId(),
                booking.getStartTime(),
                booking.getEndTime(),
                null, // itemId отсутствует в Booking
                booking.getStatus(),
                UserDtoMapper.toDto(booking.getBooker()),
                ItemDtoMapper.toDto(booking.getItem())
        );
    }

    public static Booking toEntity(BookingDto bookingDto, Item item, User booker) {
        if (bookingDto == null || item == null || booker == null) {
            throw new IllegalArgumentException("BookingDto, Item, and User must not be null");
        }
        return new Booking(
                bookingDto.getId(),
                bookingDto.getStartTime(),
                bookingDto.getEndTime(),
                item,
                booker,
                bookingDto.getStatus()
        );
    }
}
