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

    Collection<BookingResponseDto> getBookingsForOwner(Integer userId, String state);

    List<BookingResponseDto> getBookingsForUser(Integer userId, String state);

    BookingResponseDto updateStatus(Integer userId, Integer bookingId, Boolean approved);
}
