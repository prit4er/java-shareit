package ru.practicum.shareit.controller;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import jakarta.validation.constraints.Positive;
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
import ru.practicum.shareit.booking.BookingService;
import ru.practicum.shareit.booking.dto.BookingDto;
import ru.practicum.shareit.booking.dto.BookingResponseDto;

import java.util.Collection;

import static ru.practicum.shareit.constans.Constants.HEADER_USER_ID;
import static ru.practicum.shareit.constans.Constants.STATE_REGEX;

@Slf4j
@RestController
@RequiredArgsConstructor
@RequestMapping(path = "/bookings")
public class BookingController {

    private final BookingService bookingService;

    @PostMapping
    @ResponseStatus(HttpStatus.CREATED)
    public BookingResponseDto create(@Positive @RequestHeader(HEADER_USER_ID) Integer userId,
                                     @Valid @RequestBody BookingDto bookingDto) {
        log.trace("Booking is started");
        return bookingService.create(userId, bookingDto);
    }

    @GetMapping("/{bookingId}")
    public BookingResponseDto getBookingId(@Positive @RequestHeader(HEADER_USER_ID) Integer userId,
                                           @Positive @PathVariable Integer bookingId) {
        log.trace("Getting booking with id: {} by user with id: {} is started", bookingId, userId);
        return bookingService.findById(userId, bookingId);
    }

    @GetMapping
    public Collection<BookingResponseDto> getUserBookings(@Positive @RequestHeader(HEADER_USER_ID) Integer userId,
                                                           @Valid @Pattern(regexp = STATE_REGEX)
                                                           @RequestParam(required = false,
                                                                   defaultValue = "ALL") String state) {
        log.trace("Getting collection of bookings for user-owner with id: {} is started. State is: {}", userId, state);
        return bookingService.getUserBookings(userId, state);
    }

    @GetMapping("/owner")
    public Collection<BookingResponseDto> getUserAsOwnerBookings(@Positive @RequestHeader(HEADER_USER_ID) Integer userId,
                                                                  @Valid @Pattern(regexp = STATE_REGEX)
                                                                  @RequestParam(required = false,
                                                                          defaultValue = "ALL") String state) {
        log.trace("Getting collection of bookings for user-booker with id: {} is started. State is: {}",
                  userId, state);
        return bookingService.getOwnerBookings(userId, state);
    }

    @PatchMapping("/{bookingId}")
    public BookingResponseDto updateStatus(@Positive @RequestHeader(HEADER_USER_ID) Integer userId,
                                           @Positive @PathVariable Integer bookingId,
                                           @NotNull @RequestParam Boolean approved) {
        log.trace("Start of updating booking status (id: {}, userId: {})", bookingId, userId);
        return bookingService.updateStatus(userId, bookingId, approved);
    }
}