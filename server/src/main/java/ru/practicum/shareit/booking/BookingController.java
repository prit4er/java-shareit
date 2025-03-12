package ru.practicum.shareit.booking;

import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PatchMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;
import org.springframework.web.bind.annotation.RestController;
import ru.practicum.shareit.booking.dto.BookingDto;

import java.util.Collection;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    public static final String HEADER_USER_ID = "X-Sharer-User-Id";

    private final BookingServiceImpl bookingService;

    @GetMapping
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDto> getBookingsByBookerId(
            @RequestHeader(HEADER_USER_ID) long bookerId,
            @RequestParam(defaultValue = "ALL") String state) {
        log.info("Received GET request for bookings for booker with id: {} and state: {}", bookerId, state);
        BookingStatus bookingState = BookingStatus.valueOf(state.toUpperCase());
        return bookingService.getBookingsByBookerIdAndState(bookerId, bookingState);
    }

    @GetMapping("/owner")
    @ResponseStatus(HttpStatus.OK)
    public Collection<BookingDto> getBookingsByOwnerId(@RequestHeader(HEADER_USER_ID) long ownerId) {
        log.info("Received GET request for bookings for owner with id: {}", ownerId);
        return bookingService.getBookingsByOwnerId(ownerId);
    }

    @GetMapping("/{booking-id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto getBookingById(@RequestHeader(HEADER_USER_ID) long userId,
                                     @PathVariable("booking-id") long bookingId) {
        log.info("Received GET request for booking with id: {} for user with id: {}", bookingId, userId);
        return bookingService.getBookingById(bookingId, userId);
    }

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingDto createBooking(@RequestHeader(HEADER_USER_ID) long bookerId,
                                    @RequestBody BookingDto bookingDto) {
        log.info("Received POST request for booking for user with id: {}", bookerId);
        return bookingService.createBooking(bookingDto, bookerId);
    }

    @PatchMapping("/{booking-id}")
    @ResponseStatus(HttpStatus.OK)
    public BookingDto approveBooking(@RequestHeader(HEADER_USER_ID) long ownerId,
                                     @PathVariable("booking-id") long bookingId,
                                     @RequestParam boolean approved) {
        log.info("Received PATCH request for booking with id: {} by user with id: {}", bookingId, ownerId);
        return bookingService.approveBooking(bookingId, ownerId, approved);
    }
}