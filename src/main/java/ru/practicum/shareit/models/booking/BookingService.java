package ru.practicum.shareit.models.booking;

import org.springframework.transaction.annotation.Transactional;
import ru.practicum.shareit.models.booking.dto.BookingDto;
import ru.practicum.shareit.models.booking.dto.BookingResponseDto;

import java.util.Collection;
import java.util.List;

@Transactional
public interface BookingService {

    BookingResponseDto create(Integer userId, BookingDto bookingDto);

    BookingResponseDto findById(Integer userId, Integer bookingId);

    Collection<BookingResponseDto> readBookingsForOwner(Integer userId, String state);

    List<BookingResponseDto> readBookingsForUser(Integer userId, String state);

    BookingResponseDto updateStatus(Integer userId, Integer bookingId, Boolean approved);
}
