package ru.practicum.shareit.booking;

import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;
import ru.practicum.shareit.item.ItemDtoMapper;
import ru.practicum.shareit.user.UserDtoMapper;

import java.util.Objects;

@Slf4j
@Component
public class BookingDtoMapper {

    public static BookingDto toDto(Booking booking) {
        if (Objects.isNull(booking)) {
            return null;
        }
        return BookingDto.builder()
                         .id(booking.getId())
                         .start(booking.getStart())
                         .end(booking.getEnd())
                         .itemId(booking.getItem().getId())
                         .bookerId(booking.getBooker().getId())
                         .status(booking.getStatus())
                         .build();
    }

    public static Booking toEntity(BookingDto bookingDto) {
        return Booking.builder()
                      .id(bookingDto.getId())
                      .start(bookingDto.getStart())
                      .end(bookingDto.getEnd())
                      .status(bookingDto.getStatus())
                      .build();
    }

    public static BookingResponseDto toBookingResponse(Booking booking) {
        log.debug("Mapping booking to response: {}", booking);
        return BookingResponseDto.builder()
                                 .id(booking.getId())
                                 .start(booking.getStart())
                                 .end(booking.getEnd())
                                 .item(ItemDtoMapper.toDto(booking.getItem()))
                                 .booker(UserDtoMapper.toDto(booking.getBooker()))
                                 .status(booking.getStatus())
                                 .build();
    }
}