package ru.practicum.shareit.ru.practicum.booking;

import org.junit.jupiter.api.Test;
import ru.practicum.shareit.booking.Booking;
import ru.practicum.shareit.booking.BookingDtoMapper;
import ru.practicum.shareit.booking.BookingStatus;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.item.Item;
import ru.practicum.shareit.user.User;

import java.time.LocalDateTime;

import static org.junit.jupiter.api.Assertions.assertEquals;

class BookingMapperTest {

    private static final long BOOKING_ID = 1L;
    private static final long USER_ID = 1L;
    private static final long ITEM_ID = 1L;
    private static final LocalDateTime START_TIME = LocalDateTime.of(2025, 3, 11, 10, 0);
    private static final LocalDateTime END_TIME = START_TIME.plusHours(1);

    @Test
    void convertToDto() {
        // Arrange
        User booker = new User();
        booker.setUserId(USER_ID);
        Item item = new Item();
        item.setItemId(ITEM_ID);
        Booking booking = new Booking();
        booking.setId(BOOKING_ID);
        booking.setStartTime(START_TIME);
        booking.setEndTime(END_TIME);
        booking.setBooker(booker);
        booking.setItem(item);
        booking.setStatus(BookingStatus.APPROVED);

        // Act
        BookingDto result = BookingDtoMapper.toDto(booking);

        // Assert
        assertEquals(BOOKING_ID, result.getId());
        assertEquals(START_TIME, result.getStart());
        assertEquals(END_TIME, result.getEnd());
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        assertEquals(USER_ID, result.getBooker().getId());
        assertEquals(ITEM_ID, result.getItem().getId());
    }

    @Test
    void convertToEntity() {
        // Arrange
        User booker = new User();
        booker.setUserId(USER_ID);
        Item item = new Item();
        item.setItemId(ITEM_ID);
        BookingDto dto = new BookingDto();
        dto.setId(BOOKING_ID);
        dto.setStart(START_TIME);
        dto.setEnd(END_TIME);
        dto.setStatus(BookingStatus.APPROVED);

        // Act
        Booking result = BookingDtoMapper.toEntity(dto, item, booker);

        // Assert
        assertEquals(BOOKING_ID, result.getId());
        assertEquals(START_TIME, result.getStartTime());
        assertEquals(END_TIME, result.getEndTime());
        assertEquals(BookingStatus.APPROVED, result.getStatus());
        assertEquals(booker, result.getBooker());
        assertEquals(item, result.getItem());
    }
}