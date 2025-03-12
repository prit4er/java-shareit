package ru.practicum.shareit.booking.dto;

import java.util.Optional;

public enum BookingStatus {
    // Все
    ALL, CURRENT, PAST, FUTURE, WAITING, REJECTED, APPROVED;

    public static Optional<BookingStatus> from(String stringState) {
        for (BookingStatus state : values()) {
            if (state.name().equalsIgnoreCase(stringState)) {
                return Optional.of(state);
            }
        }
        return Optional.empty();
    }
}