package ru.practicum.shareit.booking;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.Collection;
import java.util.List;

@Transactional
public interface BookingService {

    BookingResponseDto create(Integer userId, BookingDto bookingDto);

    BookingResponseDto findById(Integer userId, Integer bookingId);

    Collection<BookingResponseDto> getOwnerBookings(Integer userId, BookingStatus state);

    List<BookingResponseDto> getUserBookings(Integer userId, BookingStatus state);

    BookingResponseDto updateStatus(Integer userId, Integer bookingId, Boolean approved);
}
